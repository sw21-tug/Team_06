package com.team06.focuswork.ui.tasks

import android.app.AlertDialog.*
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.team06.focuswork.R
import com.team06.focuswork.data.FireBaseFireStoreUtil
import com.team06.focuswork.data.Task
import com.team06.focuswork.databinding.FragmentNewTaskBinding
import com.team06.focuswork.model.TasksViewModel
import com.team06.focuswork.ui.util.DatePickerFragment
import com.team06.focuswork.ui.util.NotificationUtil
import com.team06.focuswork.ui.util.SnackBarUtil
import com.team06.focuswork.ui.util.TimePickerFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DateFormat.*
import java.util.*

class NewTaskFragment : Fragment() {

    private var workingTaskId: String = ""
    private var startCalendar: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance())
    private var endCalendar: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance())

    private val startDatePicker = DatePickerFragment(startCalendar)
    private val startTimePicker = TimePickerFragment(startCalendar)
    private val endDatePicker = DatePickerFragment(endCalendar)
    private val endTimePicker = TimePickerFragment(endCalendar)
    private lateinit var binding: FragmentNewTaskBinding
    private val fireBaseStore = FireBaseFireStoreUtil()
    private val tasksViewModel: TasksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentNewTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskNameView = binding.taskName
        val taskDescriptionView = binding.taskDescription

        taskNameView.doAfterTextChanged { checkTextFilled(view) }
        taskDescriptionView.doAfterTextChanged { checkTextFilled(view) }

        tasksViewModel.currentTask.value.let { task ->
            if (task != null) {
                taskNameView.setText(task.taskName)
                taskDescriptionView.setText(task.taskDescription)
                startCalendar.value = task.startTime
                endCalendar.value = task.endTime
                workingTaskId = task.id

                (activity as AppCompatActivity).supportActionBar?.title =
                    getString(R.string.title_edit_task)
                binding.taskCreate.text = getString(R.string.action_save_task)
            } else {
                endCalendar.value?.add(Calendar.HOUR, 1)

                (activity as AppCompatActivity).supportActionBar?.title =
                    getString(R.string.menu_new_task)
                binding.taskCreate.text = getString(R.string.action_create_task)
            }
        }

        prepareStartDateTextView(binding.taskStartDate)
        prepareStartTimeTextView(binding.taskStartTime)
        prepareEndDateTextView(binding.taskEndDate)
        prepareEndTimeTextView(binding.taskEndTime)
        setupObserverCallbacks()

        binding.taskCreate.setOnClickListener {
            saveTask()
            findNavController().navigateUp()
        }
        binding.taskSaveTemplate.setOnClickListener { saveTemplate() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_new_task, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_new_task_template -> showTemplates(true)
        R.id.menu_delete_task_template -> showTemplates(false)
        else -> super.onOptionsItemSelected(item)
    }

    private fun showTemplates(loadTemplate: Boolean): Boolean {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return false
        val keys = sharedPref.all.keys
        val keyNames = keys.filter { key -> key.subSequence(0, 9) == "template_" }
            .map { key -> key.substring(9) }.toTypedArray()

        val alertBuilder = Builder(context)
        if (loadTemplate) {
            alertBuilder.setTitle("Choose your template")
            alertBuilder.setItems(keyNames) { _, which ->
                val template = sharedPref
                    .getStringSet("template_${keyNames[which]}", setOf("", ""))
                binding.taskName.setText(template?.find { it[0] == 'n' }?.substring(1))
                binding.taskDescription.setText(template?.find { it[0] == 'd' }?.substring(1))
            }
        } else {
            alertBuilder.setTitle("Delete your template")
            alertBuilder.setItems(keyNames) { _, which ->
                val editor = sharedPref.edit()
                editor.remove("template_${keyNames[which]}")
                editor.apply()
            }
        }


        val dialog: android.app.AlertDialog = alertBuilder.create()
        dialog.show()
        return true
    }

    private fun saveTemplate() {
        val alertBuilder = Builder(requireContext())
        alertBuilder.setTitle("Title")

        val input = EditText(requireContext())
        input.id = R.id.templateTitle
        input.inputType = InputType.TYPE_CLASS_TEXT
        alertBuilder.setView(input)

        alertBuilder.setPositiveButton("OK") { _, _ ->
            onTemplateSavedOK(input.text.toString())
        }
        alertBuilder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        alertBuilder.show()
    }

    private fun onTemplateSavedOK(title: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val template = mutableSetOf(
            // we have 'n' or 'd' as first character, to know which is name and which is description
            // because sharedPreferences returns and unordered Set
            "n" + binding.taskName.text.toString(), "d" + binding.taskDescription.text.toString()
        )
        if (!sharedPref.contains("template_$title")) {
            with(sharedPref.edit()) {
                putStringSet("template_$title", template)
                apply()
            }
        } else {
            SnackBarUtil.showSnackBar(
                binding.root, R.string.template_exists_toast, requireActivity()
            )
        }
    }

    private fun saveTask() {
        val workingTask = Task(
            binding.taskName.text.toString(),
            binding.taskDescription.text.toString(),
            startCalendar.value ?: return,
            endCalendar.value ?: return
        )
        workingTask.id = workingTaskId
        requireContext().let {
            GlobalScope.launch {
                delay(workingTask.endTime.timeInMillis - System.currentTimeMillis())
                NotificationUtil.sendTimerFinishedNotif(it, workingTask)
            }
        }
        fireBaseStore.saveTask(workingTask, tasksViewModel::setSelectedTask)
    }

    private fun prepareStartTimeTextView(startTimeTextView: TextView) {
        startTimeTextView.text = startCalendar.value?.let { formatTime(it) }
        startTimeTextView.setOnClickListener {
            startTimePicker.arguments = createDateOrTimeBundle(isDate = false, startBundle = true)
            startTimePicker.show(childFragmentManager, TimePickerFragment.TAG)
        }
    }

    private fun prepareStartDateTextView(taskStartDate: TextView) {
        taskStartDate.text = startCalendar.value?.let { formatDate(it) }
        taskStartDate.setOnClickListener {
            startDatePicker.arguments = createDateOrTimeBundle(isDate = true, startBundle = true)
            startDatePicker.show(childFragmentManager, DatePickerFragment.TAG)
        }
    }

    private fun prepareEndTimeTextView(endTimeTextView: TextView) {
        endTimeTextView.text = endCalendar.value?.let { formatTime(it) }
        endTimeTextView.setOnClickListener {
            endTimePicker.arguments = createDateOrTimeBundle(isDate = false, startBundle = false)
            endTimePicker.show(childFragmentManager, TimePickerFragment.TAG)
        }
    }

    private fun prepareEndDateTextView(endDateTextView: TextView) {
        endDateTextView.text = endCalendar.value?.let { formatDate(it) }
        endDateTextView.setOnClickListener {
            endDatePicker.arguments = createDateOrTimeBundle(isDate = true, startBundle = false)
            endDatePicker.show(childFragmentManager, DatePickerFragment.TAG)
        }
    }

    private fun setupObserverCallbacks() {
        startCalendar.observe(viewLifecycleOwner, androidx.lifecycle.Observer { cal ->
            val nowCal = Calendar.getInstance()
            nowCal.add(Calendar.MINUTE, -1)
            if (cal.before(nowCal)) {
                cal.add(Calendar.DAY_OF_YEAR, 1)
                startCalendar.value = nowCal
            }
            if (endCalendar.value?.before(startCalendar.value) ?: return@Observer) {
                endCalendar.postValue(startCalendar.value?.clone() as GregorianCalendar)
            }
            updateTextFields()
        })
        endCalendar.observe(viewLifecycleOwner, { cal ->
            if (cal.before(startCalendar.value)) {
                endCalendar.value = startCalendar.value?.clone() as GregorianCalendar
            }
            updateTextFields()
        })
    }

    private fun updateTextFields() {
        binding.taskEndDate.text = endCalendar.value?.let { formatDate(it) }
        binding.taskEndTime.text = endCalendar.value?.let { formatTime(it) }
        binding.taskStartDate.text = startCalendar.value?.let { formatDate(it) }
        binding.taskStartTime.text = startCalendar.value?.let { formatTime(it) }
    }

    private fun formatDate(cal: Calendar): String {
        return getDateInstance(MEDIUM).format(cal.time)
    }

    private fun formatTime(cal: Calendar): String {
        return getTimeInstance(SHORT).format(cal.time)
    }

    private fun checkTextFilled(view: View) {
        val enabled = !(view.findViewById<TextView>(R.id.taskName).text.isBlank() ||
            view.findViewById<TextView>(R.id.taskDescription).text.isBlank())
        view.findViewById<Button>(R.id.taskCreate).isEnabled = enabled
        view.findViewById<Button>(R.id.taskSaveTemplate).isEnabled = enabled
    }

    private fun createDateOrTimeBundle(isDate: Boolean, startBundle: Boolean): Bundle {
        val cal = if (startBundle) startCalendar.value else endCalendar.value
        return if (isDate) bundleOf(
            Pair("YEAR", cal?.get(Calendar.YEAR)),
            Pair("MONTH", cal?.get(Calendar.MONTH)),
            Pair("DAY", cal?.get(Calendar.DAY_OF_MONTH)),
            Pair(
                "MIN_DATE",
                if (startBundle) System.currentTimeMillis() - 1000
                else startCalendar.value?.timeInMillis
            )
        ) else bundleOf(
            Pair("HOUR", cal?.get(Calendar.HOUR_OF_DAY)),
            Pair("MINUTE", cal?.get(Calendar.MINUTE))
        )
    }
}