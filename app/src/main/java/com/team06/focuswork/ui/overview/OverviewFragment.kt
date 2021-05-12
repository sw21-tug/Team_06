package com.team06.focuswork.ui.overview

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
import com.team06.focuswork.ui.util.NotificationUtil.createNotifChannels
import com.team06.focuswork.ui.util.NotificationUtil.sendTimerFinishedNotif

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

        createNotifChannels(requireContext())
        binding.notifButton.setOnClickListener(this::sendNotif)

        fireStoreUtil.retrieveTasks(this::setTasks)

        when (filter) {
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

    private fun initializeDayView() {
        val localBinding = dynamicBinding as FragmentDayBinding

        initDayUI(localBinding)

        recyclerView = localBinding.recyclerViewWeek
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        localBinding.progressbar.visibility = View.GONE
        recyclerView.adapter = TaskAdapter(requireContext(), this)

        tasksViewModel.allTasks.observe(requireActivity(), Observer { tasks ->
            currentTasks.removeAll(currentTasks)
            tasks.iterator().forEach {
                if (filterForDay(Calendar.getInstance(), it.startTime, it.endTime))
                    currentTasks.add(it)
            }
            (recyclerView.adapter as TaskAdapter).notifyDataSetChanged()
        })
    }

    private fun initDayUI(binding: FragmentDayBinding) {
        val cal = Calendar.getInstance()

        val dayName = SimpleDateFormat("EEEE").format(cal.time)
        val text = SpannableString(
            String.format(
                "%s %s, %d.%d.", getString(R.string.day_tasks_for),
                dayName, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1
            )
        )
        text.setSpan(
            StyleSpan(Typeface.BOLD), getString(R.string.day_tasks_for).length + 1,
            dayName.length + getString(R.string.day_tasks_for).length + 2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textviewTitle.text = text
    }

    private fun initializeWeekView() {
        val localBinding = dynamicBinding as FragmentWeekBinding

        initWeekUI(localBinding)
        initWeekButtons(localBinding)

        recyclerView = localBinding.recyclerViewWeek
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        localBinding.progressbar.visibility = View.GONE
        recyclerView.adapter = TaskAdapter(requireContext(), this)

        tasksViewModel.allTasks.observe(requireActivity(), Observer { tasks ->
            currentTasks.removeAll(currentTasks)
            tasks.iterator().forEach {
                if (filterForDay(selectedDay, it.startTime, it.endTime))
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


        val text = SpannableString(
            String.format(
                "%s, %2d.%2d.  -  %s, %2d.%2d.",
                monday, calMon.get(Calendar.DAY_OF_MONTH), calMon.get(Calendar.MONTH) + 1,
                sunday, calSun.get(Calendar.DAY_OF_MONTH), calSun.get(Calendar.MONTH) + 1
            )
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

    private fun initWeekButtons(binding: FragmentWeekBinding) {
        binding.buttonDisplayAll.setOnClickListener {
            showEntireWeek = true
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonMonday.setOnClickListener {
            setMonday(selectedDay)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonTuesday.setOnClickListener {
            setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 1)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonWednesday.setOnClickListener {
            setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 2)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonThursday.setOnClickListener {
            setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 3)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonFriday.setOnClickListener {
            setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 4)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonSaturday.setOnClickListener {
            setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 5)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
        binding.buttonSunday.setOnClickListener {
            setMonday(selectedDay)
            selectedDay.add(Calendar.DAY_OF_MONTH, 6)
            showEntireWeek = false
            tasksViewModel.setTasks(allTasks)
        }
    }

    private fun filterForDay(day: Calendar, start: Calendar, end: Calendar): Boolean {
        if (showEntireWeek) return filterForWeek(day, start, end)

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

    //week can just be any day within the week
    private fun filterForWeek(week: Calendar, start: Calendar, end: Calendar): Boolean {
        setMonday(week)

        val startDay = start.get(Calendar.DAY_OF_YEAR) +
            start.get(Calendar.YEAR) * 366
        val endDay = end.get(Calendar.DAY_OF_YEAR) +
            end.get(Calendar.YEAR) * 366
        val currentDay = week.get(Calendar.DAY_OF_YEAR) +
            week.get(Calendar.YEAR) * 366

        return (currentDay in startDay..endDay) || (currentDay + 1 in startDay..endDay) ||
            (currentDay + 2 in startDay..endDay) || (currentDay + 3 in startDay..endDay) ||
            (currentDay + 4 in startDay..endDay) || (currentDay + 5 in startDay..endDay) ||
            (currentDay + 6 in startDay..endDay)
    }


    private fun sendNotif(@Suppress("UNUSED_PARAMETER") view :View) {
        sendTimerFinishedNotif(requireContext())
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

    companion object Overview {
        //This is needed because setting the field Calendar.DAY_OF_WEEK has flimsy behaviour
        fun setMonday(cal: Calendar): Calendar {
            var delta = 0
            when (cal.get(Calendar.DAY_OF_WEEK)) {
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
    }
}