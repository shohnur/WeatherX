package uz.myweatherapp.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.model_data.view.*
import uz.myweatherapp.R
import uz.myweatherapp.core.model.daily.Daily
import java.text.SimpleDateFormat
import java.util.*

class RVAdapter : RecyclerView.Adapter<RVAdapter.DataViewHolder>() {


    var data= listOf<Daily>()

    inner class DataViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bindData(d:Daily){
            itemView.temp.text= String
                .format("%s°C\n%s°C"
                    , String.format("%.1f",d.temp!!.day!!-273.15),String.format("%.1f", d.temp.night!!-273.15))
            val dt=Date((d.dt!!.toLong().times(1000)))
            val sdf = SimpleDateFormat("dd/MM")
            itemView.date.text=sdf.format(dt)

            itemView.setOnClickListener {
                onItemSelected?.onItemSelected(d)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.model_data,parent,false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int)=holder.bindData(data[position])

    override fun getItemCount(): Int =data.size

    interface OnItemSelected{
        fun onItemSelected(d:Daily)
    }

    var onItemSelected:OnItemSelected?=null
}