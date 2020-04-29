package at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step
import kotlinx.android.synthetic.main.item_summarized_recipe.view.*

class InstructionAdapter(val context : Context, val instructions : List<Step>)
    : BaseAdapter() {

    private val inflater : LayoutInflater = context.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val viewHolder : ViewHolder

        if(convertView == null) {
            view = inflater.inflate(R.layout.listitem_instruction, parent, false)

            viewHolder = ViewHolder()
            viewHolder.tvStepNumber = view.findViewById(R.id.li_instruction_number) as TextView
            viewHolder.tvInstruction = view.findViewById(R.id.li_instruction_text) as TextView
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val instruction = getItem(position) as Step

        viewHolder.tvStepNumber.text = "Step ${position + 1}"
        viewHolder.tvInstruction.text = instruction.name

        return view
    }

    override fun getItem(position: Int): Any {
        return instructions[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return instructions.size
    }

    private class ViewHolder {
        lateinit var tvStepNumber : TextView
        lateinit var tvInstruction : TextView
    }
}