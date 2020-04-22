package at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step

class InstructionAdapter(val context : Context, val steps : List<Step>, private val activity: FragmentActivity) : BaseAdapter() {
    private val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val viewHolder : ViewHolder

        if(convertView == null) {
            view = inflater.inflate(R.layout.item_instruction, parent, false)

            viewHolder = ViewHolder()
            viewHolder.step = view.findViewById(R.id.step_text) as TextView
            viewHolder.stepDescription = view.findViewById(R.id.step_desc) as TextView

            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val step = getItem(position) as Step
        viewHolder.step.text = context.getString(R.string.recipe_step, position+1)
        viewHolder.stepDescription.text = step.name

        return view
    }

    override fun getItem(position: Int): Any {
        return steps[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return steps.size
    }

    private class ViewHolder {
        lateinit var step : TextView
        lateinit var stepDescription : TextView
    }
}