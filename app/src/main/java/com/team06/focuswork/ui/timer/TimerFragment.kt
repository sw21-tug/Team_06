package com.team06.focuswork.ui.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import com.team06.focuswork.R
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.team06.focuswork.MainActivity
import com.team06.focuswork.data.Task
import com.team06.focuswork.databinding.FragmentTimerBinding
import java.text.DateFormat
import java.time.Duration
import java.util.*
import kotlin.math.truncate
import androidx.core.content.ContextCompat.getSystemService

/**
 * A simple [Fragment] subclass.
 * Use the [TimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding
    private val list = mutableListOf<Task>()
    init {
        populateList()
    }

    private var selectedTask : Task? = null
    private var selectedTaskTimer : CountDownTimer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateSpinner()
        createNotifChannel()

        binding.buttonStartTimer.setOnClickListener{
            if(selectedTask == null)
            {
                showToast(getString(R.string.no_task_selected))
                return@setOnClickListener
            }
            val duration = selectedTask!!.endTime.timeInMillis -
                    selectedTask!!.startTime.timeInMillis





            selectedTaskTimer = object: CountDownTimer(duration, 10 * 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val hours = millisUntilFinished / (60 * 60 * 1000)
                    val minutes = millisUntilFinished / (60 * 1000) % (60)
                    binding.taskTimer.text = String.format("%d:%02d", hours, minutes)
                    binding.durationLeft.text = String.format("%d:%02d", hours, minutes)
                    updateTimerAnimation(duration, millisUntilFinished, hours, minutes)
                  }

                override fun onFinish() {
                    showToast(getString(R.string.timer_finished))
                    sendNotif(view)
                }
            }.start()
        }
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
            val notificationManager = ContextCompat.getSystemService(requireContext(),
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
                .setContentTitle("Your task has finished!")
                .setContentText("The task {...} you have set has finished.")
                .setStyle(NotificationCompat.BigTextStyle().bigText("This text is so much longer than the original message."))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(notificationSound)


        with(NotificationManagerCompat.from(requireContext())) {
            notify(101, builder.build())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun updateTimerAnimation(duration: Long, millisUntilFinished: Long,
                                     hours: Long, minutes: Long) {
        binding.timerAnimation.progress = ((millisUntilFinished.toFloat() / duration.toFloat()) * 100)
                .toInt()
        binding.durationLeft.text = String.format("%d:%02d", hours, minutes)
    }


    fun populateList(){
        //Intended: call data holder class to fetch Tasks
        val calLater = Calendar.getInstance()
        calLater.add(Calendar.HOUR, 1)
        list.add(
            Task(
                "Erste Aufgabe",
                "Dies ist eine Aufgabenbeschr.",
                Calendar.getInstance(),
                calLater
            )
        )
        list.add(
            Task(
                "Zweite Aufgabe",
                "Dies ist weitere Aufgabenbeschr.",
                Calendar.getInstance(),
                calLater
            )
        )
        list.add(
            Task(
                "Dritte Aufgabe",
                "Dies ist noch eine Aufgabenbeschr.",
                Calendar.getInstance(),
                calLater
            )
        )
        list.add(
            Task(
                "Vierte Aufgabe",
                "Dies ist auch eine Aufgabenbeschr.",
                Calendar.getInstance(),
                calLater
            )
        )
        list.add(
            Task(
                "Fuenfte Aufgabe",
                "Dies ist eine tolle Aufgabenbeschr.",
                Calendar.getInstance(),
                calLater
            )
        )
        list.add(
            Task(
                "Sechste Aufgabe",
                "Dies ist eine tolle Aufgabenbeschr.",
                Calendar.getInstance(),
                calLater
            )
        )
    }

    fun populateSpinner(){
        val spinner = binding.taskSelector
        val adapter = TaskArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            list
        )
        spinner.adapter = adapter

        spinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?, view: View,
                position: Int, id: Long
            ) {
                selectedTask = adapter.getItem(position)
                selectedTaskTimer?.cancel()
                binding.taskTimer.text = "0:00"
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {
                selectedTask = null
                selectedTaskTimer?.cancel()
                binding.taskTimer.text = "0:00"
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context?.applicationContext, message, Toast.LENGTH_LONG).show()
    }
}

class TaskArrayAdapter(context: Context, textViewResourceId: Int, val tasks: MutableList<Task>)
    : ArrayAdapter<Task>(context, textViewResourceId) {

    override fun getCount() : Int {
        return tasks.size
    }

    override fun getItem(position: Int) : Task {
        return tasks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK);
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30.0f)
        label.setText(tasks[position].taskName)

        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        //Could be changed if dropdown should have a different style from the 'passive'
        return getView(position, convertView, parent)
    }
}