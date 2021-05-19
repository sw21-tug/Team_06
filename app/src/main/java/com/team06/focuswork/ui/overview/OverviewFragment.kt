package com.team06.focuswork.ui.overview

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.team06.focuswork.MainActivity
import com.team06.focuswork.R
import com.team06.focuswork.data.FireBaseFireStoreUtil
import com.team06.focuswork.data.FireBaseFireStoreUtil.Filter
import com.team06.focuswork.data.Task
import com.team06.focuswork.databinding.FragmentDayBinding
import com.team06.focuswork.databinding.FragmentOverviewBinding
import com.team06.focuswork.databinding.FragmentWeekBinding
import com.team06.focuswork.databinding.FragmentMonthBinding
import com.team06.focuswork.model.TasksViewModel
import com.team06.focuswork.ui.util.FilterUtil
import java.text.SimpleDateFormat
import java.util.*

class OverviewFragment : Fragment() {

    private val tasksViewModel: TasksViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentOverviewBinding
    private lateinit var dynamicBinding: ViewBinding
    private val fireStoreUtil = FireBaseFireStoreUtil()
    private val currentTasks = mutableListOf<Task>()
    private val allTasks = mutableListOf<Task>()
    private var filter: Filter = Filter.NONE
    private var selectedDay = Calendar.getInstance()
    private var showEntireWeek = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewBinding.inflate(layoutInflater, container, false)
        val layout = binding.fragmentContainerOverview

        readFilterFromConfig()

        when (filter) {
            Filter.DAY -> {
                dynamicBinding = FragmentDayBinding.inflate(layoutInflater, layout, false)
                layout.addView((dynamicBinding as FragmentDayBinding).fragmentContainerDay)
            }
            Filter.WEEK -> {
                dynamicBinding = FragmentWeekBinding.inflate(layoutInflater, layout, false)
                layout.addView((dynamicBinding as FragmentWeekBinding).container)
            }
            Filter.MONTH -> {
                dynamicBinding = FragmentMonthBinding.inflate(layoutInflater, layout, false)
                layout.addView((dynamicBinding as FragmentMonthBinding).fragmentContainerMonth)
            }
            else -> {
                showToast(R.string.erroneous_config)
                dynamicBinding = FragmentWeekBinding.inflate(layoutInflater, layout, false)
                layout.addView((dynamicBinding as FragmentWeekBinding).container)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createNotifChannel()
        binding.notifButton.setOnClickListener { sendNotif() }

        fireStoreUtil.retrieveTasks(this::setTasks)
        val fab: FloatingActionButton = binding.fab
        fab.setOnClickListener { _ ->
            tasksViewModel.setSelectedTask(null)
            findNavController().navigate(R.id.action_nav_overview_to_nav_new_task)
        }
        when(filter){
            Filter.DAY -> initializeDayView()
            Filter.WEEK -> initializeWeekView()
            Filter.MONTH -> initializeMonthView()
            else -> {
                //TODO: Error handling
                initializeWeekView()
            }
        }
    }

    private fun setTasks(tasks: List<Task>) {
        allTasks.removeAll(allTasks)
        allTasks.addAll(tasks)
        tasksViewModel.setTasks(tasks)
    }

    private fun initializeDayView() {
        val localBinding = dynamicBinding as FragmentDayBinding

        localBinding.textViewDay.text = FilterUtil.getDayText(Calendar.getInstance(), requireContext())
        recyclerView = localBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        localBinding.progressbar.visibility = View.GONE
        recyclerView.adapter = TaskAdapter(requireContext(), this)
        tasksViewModel.setSelectedTask(null)

        tasksViewModel.allTasks.observe(requireActivity(), Observer {
                tasks -> currentTasks.removeAll(currentTasks)
            tasks.iterator().forEach {
                if (FilterUtil.filterForDay(Calendar.getInstance(), it.startTime, it.endTime))
                    currentTasks.add(it)
            }
            (recyclerView.adapter as TaskAdapter).notifyDataSetChanged()
        })
    }

    private fun initializeWeekView() {
        val localBinding = dynamicBinding as FragmentWeekBinding

        localBinding.textViewWeek.text =
            FilterUtil.getWeekText(Calendar.getInstance(), Calendar.getInstance())
        initWeekButtons(localBinding)

        recyclerView = localBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        localBinding.progressbar.visibility = View.GONE
        recyclerView.adapter = TaskAdapter(requireContext(), this)

        tasksViewModel.allTasks.observe(requireActivity(), Observer { tasks ->
            currentTasks.removeAll(currentTasks)
            tasks.iterator().forEach {
                if (showEntireWeek && FilterUtil.filterForWeek(
                        selectedDay,
                        it.startTime,
                        it.endTime
                    ) ||
                    !showEntireWeek && FilterUtil.filterForDay(
                        selectedDay,
                        it.startTime,
                        it.endTime
                    )
                )
                    currentTasks.add(it)
            }
            (recyclerView.adapter as TaskAdapter).notifyDataSetChanged()
        })
    }

    private fun initializeMonthView() {
        val localBinding = dynamicBinding as FragmentMonthBinding

        localBinding.textviewMonth.text = FilterUtil.getMonthText(Calendar.getInstance(), requireContext())
        recyclerView = localBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        localBinding.progressbar.visibility = View.GONE
        recyclerView.adapter = TaskAdapter(requireContext(), this)

        tasksViewModel.allTasks.observe(requireActivity(), Observer { tasks ->
            currentTasks.removeAll(currentTasks)
            tasks.iterator().forEach {
                if (FilterUtil.filterForMonth(Calendar.getInstance(), it.startTime, it.endTime))
                    currentTasks.add(it)
            }
            (recyclerView.adapter as TaskAdapter).notifyDataSetChanged()
        })
    }

    private fun initWeekButtons(binding: FragmentWeekBinding) {
        binding.buttonDisplayAll.setOnClickListener {
            showEntireWeek = true
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonMonday.setOnClickListener {
            FilterUtil.setMonday(selectedDay)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonTuesday.setOnClickListener {
            FilterUtil.setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 1)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonWednesday.setOnClickListener {
            FilterUtil.setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 2)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonThursday.setOnClickListener {
            FilterUtil.setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 3)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonFriday.setOnClickListener {
            FilterUtil.setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 4)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonSaturday.setOnClickListener {
            FilterUtil.setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 5)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonSunday.setOnClickListener {
            FilterUtil.setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 6)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
    }

    private fun createNotifChannel() {
        // based on this tutorial
        // https://www.youtube.com/watch?v=B5dgmvbrHgs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer finished"
            val descriptionText = "The timer for your task has finished."
            val important = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("TIMER_NOTIF_ID", name, important).apply {
                description = descriptionText;
            }
            val notificationManager = getSystemService(
                requireContext(),
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotif() {
        // based on this tutorial
        // out first notification, navigates back to app by clicking on it
        // https://www.youtube.com/watch?v=B5dgmvbrHgs
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(requireContext(), 0, intent, 0)

        val notificationSound: Uri = RingtoneManager.getDefaultUri(
            RingtoneManager
                .TYPE_NOTIFICATION
        )

        val builder = NotificationCompat.Builder(requireContext(), "TIMER_NOTIF_ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText("The task {...} you have set has finished.")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(getString(R.string.notification_message))
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(notificationSound)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(101, builder.build())
        }
    }

    fun onClickTaskItem(task: Task) {
        tasksViewModel.setSelectedTask(task)
        findNavController().navigate(R.id.action_nav_overview_to_nav_taskdetails)
    }

    fun getAllTasks(): MutableList<Task> = currentTasks

    private fun readFilterFromConfig() {
        val preferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext())

        val filter = (preferences.getString("overviewTimeFrame", "none")).toString()
        Log.d("overview", filter)
        when (filter) {
            "day" -> this.filter = Filter.DAY
            "week" -> this.filter = Filter.WEEK
            "month" -> this.filter = Filter.MONTH
            else -> {
                this.filter = Filter.WEEK
            }
        }
    }

    private fun showToast(@StringRes string: Int) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }
}