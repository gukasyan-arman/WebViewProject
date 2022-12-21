package winlain.winline.amboss.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.databinding.FragmentMainBinding
import winlain.winline.amboss.model.Exercise

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var exerciseAdapter: ExerciseAdapter
    private val listOfExercises = listOf(
        Exercise("Bench press", "Chest", "basic exercise for chest, shoulders and arms", "Basic", R.drawable.bench_press),
        Exercise("Back squat", "Legs", "Just like the deadlift, the barbell back squat hits just about every major muscle group there is in the body and is the king of leg-developing movements. Any athlete will tout the squat as the reason they run fast, jump high, and keep increasing in strength all over.", "Basic", R.drawable.back_squat),
        Exercise("Incline dumbbell flye", "Chest", "Lie on an incline bench holding a dumbbell in each hand above your face, with your palms facing and a slight bend in your elbows. Lower them to the sides, then bring them back to the top.", "Basic", R.drawable.incline_dumbbell_fly),
        Exercise("Triceps extension", "Triceps", "Sit down on bench holding a dumbbell over your head with both hands, arms straight. Keeping your chest up, lower the weight behind your head, then raise it back to the start.", "Isolation", R.drawable.seated_dumbbell_tricep_extension),
        Exercise("Pull-up", "back", "Hold a pull-up bar with an overhand grip, hands shoulder-width apart. Brace your core, then pull yourself up until your lower chest touches the bar. Lower until your arms are straight again.", "Basic", R.drawable.pull_up),
        Exercise("Standing biceps curl", "biceps", "Stand with dumbbells by your sides, palms facing forwards. Keeping your elbows tucked in, curl the weights up, squeezing your biceps at the top. Lower them back to the start.", "Isolation", R.drawable.standing_biceps_curl),
        Exercise("Deadlift", "Legs", "The deadlift is a compound exercise that has you lifting heavy weight off the floor by bending at the hips and standing back up.", "Basic", R.drawable.deadlift),
        Exercise("Barbell press", "Shoulders", "Sit on the bench holding a barbell in front of your shoulders with an overhand grip. Press the weight up above your head until your arms are fully extended. Return slowly to the start position.", "Basic", R.drawable.seated_barbell_press),
    )
    private var searchedList = mutableListOf<Exercise>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initStartAdapter()

        binding.searchButton.setOnClickListener {
            if (binding.searchInput.text.trim().isNotEmpty()) {
                for (exercise in listOfExercises) {
                    if (exercise.name.lowercase() == binding.searchInput.text.toString().lowercase().trim()) {
                        searchedList.add(exercise)
                    }
                    else if (exercise.target.lowercase() == binding.searchInput.text.toString().lowercase().trim()) {
                        searchedList.add(exercise)
                    } else {
                        binding.resultsNotFoundText.visibility = View.VISIBLE
                        binding.exerciseList.visibility = View.GONE
                    }
                }
                if (searchedList.isEmpty()) {
                    binding.resultsNotFoundText.visibility = View.VISIBLE
                    binding.exerciseList.visibility = View.GONE
                } else {
                    binding.resultsNotFoundText.visibility = View.GONE
                    binding.exerciseList.visibility = View.VISIBLE
                    initSearchedAdapter()
                }
            } else {
                binding.resultsNotFoundText.visibility = View.GONE
                binding.exerciseList.visibility = View.VISIBLE
                initStartAdapter()
                searchedList.clear()
            }
        }

    }

    private fun initStartAdapter(){
        exerciseAdapter = ExerciseAdapter(exercises = listOfExercises)
        binding.exerciseList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.exerciseList.adapter = exerciseAdapter
    }

    private fun initSearchedAdapter(){
        exerciseAdapter = ExerciseAdapter(exercises = searchedList)
        binding.exerciseList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.exerciseList.adapter = exerciseAdapter
    }

}