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
import com.team06.focuswork.MainActivity
import com.team06.focuswork.R
import com.team06.focuswork.data.FireBaseFireStoreUtil
import com.team06.focuswork.data.FireBaseFireStoreUtil.Filter
import com.team06.focuswork.data.Task
import com.team06.focuswork.databinding.FragmentDayBinding
import com.team06.focuswork.databinding.FragmentOverviewBinding
import com.team06.focuswork.databinding.FragmentWeekBinding
import com.team06.focuswork.model.TasksViewModel
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


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewBinding.inflate(layoutInflater, container, false)
        var layout = binding.fragmentContainerOverview

        readFilterFromConfig()

        when(filter){
            Filter.DAY -> {
                dynamicBinding = FragmentDayBinding.inflate(layoutInflater, layout, false)
                layout.addView((dynamicBinding as FragmentDayBinding).container)
            }
            Filter.WEEK -> {
                dynamicBinding = FragmentWeekBinding.inflate(layoutInflater, layout, false)
                layout.addView((dynamicBinding as FragmentWeekBinding).container)
            }
            else -> {
                //TODO: Error handling
            }
        }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createNotifChannel()
        binding.notifButton.setOnClickListener(this::sendNotif)

        fireStoreUtil.retrieveTasks(this::setTasks, filter)

        when(filter){
            Filter.DAY -> initializeDayView()
            Filter.WEEK -> initializeWeekView()
            else -> {
                //TODO: Error handling
            }
        }
    }

    private fun setTasks(tasks: List<Task>) {
        allTasks.removeAll(allTasks)
        allTasks.addAll(tasks)
        tasksViewModel.setTasks(tasks)
    }

    private fun initializeDayView(){
        var localBinding = dynamicBinding as FragmentDayBinding

        initDayUI(localBinding)

        recyclerView = localBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        localBinding.progressbar.visibility = View.GONE
        recyclerView.adapter = TaskAdapter(requireContext(), this)

        tasksViewModel.allTasks.observe(requireActivity(), Observer {
                tasks -> currentTasks.removeAll(currentTasks)
            tasks.iterator().forEach {
                if(filterForDay(Calendar.getInstance(), it.startTime, it.endTime))
                    currentTasks.add(it)
            }
            (recyclerView.adapter as TaskAdapter).notifyDataSetChanged()
        })
    }

    private fun initDayUI(binding: FragmentDayBinding){
        val cal = Calendar.getInstance()

        val dayName = SimpleDateFormat("EEEE").format(cal.time)
        val text = SpannableString(String.format("%s %s, %d.%d.", getString(R.string.day_tasks_for),
                dayName, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1))
        text.setSpan(
                StyleSpan(Typeface.BOLD), getString(R.string.day_tasks_for).length+1,
                dayName.length + getString(R.string.day_tasks_for).length + 2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textviewTitle.text = text
    }

    private fun initializeWeekView(){
        var localBinding = dynamicBinding as FragmentWeekBinding

        initWeekUI(localBinding)
        initWeekButtons(localBinding)

        recyclerView = localBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        localBinding.progressbar.visibility = View.GONE
        recyclerView.adapter = TaskAdapter(requireContext(), this)

        tasksViewModel.allTasks.observe(requireActivity(), Observer {
            tasks -> currentTasks.removeAll(currentTasks)
            Log.d("Overview", "Entered Observer Function")
            tasks.iterator().forEach {
                if(filterForDay(selectedDay, it.startTime, it.endTime))
                    currentTasks.add(it)
            }
            (recyclerView.adapter as TaskAdapter).notifyDataSetChanged()
        })
    }

    private fun initWeekUI(binding: FragmentWeekBinding) {
        val calMon = Calendar.getInstance()
        setMonday(calMon)
        val monday = SimpleDateFormat("EEEE").format(calMon.time)

        val calSun = Calendar.getInstance()
        setMonday(calSun)
        calSun.add(Calendar.DATE, 6)
        val sunday = SimpleDateFormat("EEEE").format(calSun.time)


        val text = SpannableString(String.format("%s, %2d.%2d.  -  %s, %2d.%2d.",
                monday, calMon.get(Calendar.DAY_OF_MONTH), calMon.get(Calendar.MONTH) + 1,
                sunday, calSun.get(Calendar.DAY_OF_MONTH), calSun.get(Calendar.MONTH) + 1)
        )

        text.setSpan(
                StyleSpan(Typeface.BOLD), 0,
                monday.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text.setSpan(
                StyleSpan(Typeface.BOLD), monday.length + 13,
                monday.length + sunday.length + 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textviewRange.text = text
    }

    private fun initWeekButtons(binding: FragmentWeekBinding){
        binding.buttonMonday.setOnClickListener {
            setMonday(selectedDay)
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonTuesday.setOnClickListener {
            setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 1)
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonWednesday.setOnClickListener {
            setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 2)
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonThursday.setOnClickListener {
            setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 3)
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonFriday.setOnClickListener {
            setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 4)
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonSaturday.setOnClickListener {
            setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 5)
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonSunday.setOnClickListener {
            setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 6)
            tasksViewModel.setTasks(allTasks)
        }
    }

    //This is needed because setting the field Calendar.DAY_OF_WEEK has flimsy behaviour
    private fun setMonday(cal: Calendar): Calendar {
        var delta = 0
        when(cal.get(Calendar.DAY_OF_WEEK)){
            Calendar.MONDAY -> delta = 0
            Calendar.TUESDAY -> delta = -1
            Calendar.WEDNESDAY -> delta = -2
            Calendar.THURSDAY -> delta = -3
            Calendar.FRIDAY -> delta = -4
            Calendar.SATURDAY -> delta = -5
            Calendar.SUNDAY -> delta = -6
        }

        cal.add(Calendar.DAY_OF_MONTH, delta)
        return cal
    }

    private fun filterForDay(day: Calendar, start: Calendar, end: Calendar): Boolean {
        //We don't care about the year-to-day conversion being accurate
        //since we only care about what day comes first, meaning leaving 1 day unsused
        //for most years is of no consequence
        val startDay = start.get(Calendar.DAY_OF_YEAR) +
                start.get(Calendar.YEAR) * 366
        val endDay = end.get(Calendar.DAY_OF_YEAR) +
                end.get(Calendar.YEAR) * 366
        val currentDay = day.get(Calendar.DAY_OF_YEAR) +
                day.get(Calendar.YEAR) * 366

        return currentDay in startDay..endDay
    }

    private fun createNotifChannel() {
        // based off this tutorial
        // https://www.youtube.com/watch?v=B5dgmvbrHgs

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer finished"
            val descriptionText = "The timer for your task has finished."
            val important = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("TIMER_NOTIF_ID", name, important).apply {
                description = descriptionText;
            }
            val notificationManager = getSystemService(requireContext(),
                    NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotif(view: View) {
        // based off this tutorial
        // out first notification, navigates back to app by clicking on it
        // https://www.youtube.com/watch?v=B5dgmvbrHgs
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
                PendingIntent.getActivity(requireContext(), 0, intent, 0)

        val notificationSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager
                .TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(requireContext(), "TIMER_NOTIF_ID")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText("The task {...} you have set has finished.")
                .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.notification_message)))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(notificationSound)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(101, builder.build())
        }
    }

    fun onClickTaskItem(task: Task) {
        tasksViewModel.setSelectedTask(task, requireContext())
        findNavController().navigate(R.id.action_nav_overview_to_nav_taskdetails)
    }

    fun getAllTasks() : MutableList<Task> = currentTasks

    private fun readFilterFromConfig(){
        val preferences : SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext())

        var filter = (preferences.getString("overviewTimeFrame", "none")).toString()
        Log.d("overview", filter)
        when(filter)
        {
            "day" -> this.filter = Filter.DAY
            "week" -> this.filter = Filter.WEEK
            "month" -> this.filter = Filter.MONTH
            else -> {
                this.filter = Filter.WEEK
            }
        }
    }
}