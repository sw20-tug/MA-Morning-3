package at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step
import kotlinx.android.synthetic.main.item_ingredient.view.*
import kotlinx.android.synthetic.main.item_instruction.view.*

class InstructionAdapter(val context : Context, val instructions : List<Step>) :
        RecyclerView.Adapter<InstructionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val step = view.step_text
        val discription = view.step_desc
    }

    override fun getItemCount(): Int {
        return instructions.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_instruction, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.step.text = context.getString(R.string.recipe_step, position + 1)
        holder.discription.text = instructions[position].name
    }
}