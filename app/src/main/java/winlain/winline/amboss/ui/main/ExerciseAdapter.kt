package winlain.winline.amboss.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.databinding.ListItemBinding
import winlain.winline.amboss.model.Exercise

class ExerciseAdapter(private val exercises: List<Exercise>): RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exercises[position]
        with(holder){
            with(exercise){
                binding.exerciseName.text = this.name
                binding.exerciseDescription.text = this.description
                binding.exerciseTarget.text = this.target
                binding.exerciseImage.setImageResource(this.image)
            }
        }
    }

    override fun getItemCount(): Int = exercises.size
}