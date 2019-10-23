package tads.eaj.ufrn.exemploswipedragdrop

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import java.util.*
import java.util.Collections.swap
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class FrutaAdapter(var c:Context, var frutas:MutableList<Fruta>) : RecyclerView.Adapter<FrutaViewHolder>() {

    private val PENDING_REMOVAL_TIMEOUT:Long = 3000 // 3sec
    var itemsPendingRemoval = ArrayList<Fruta>()

    private val handler = Handler() // hanlder que vai guardar os runnables que devem ser executados
    var pendingRunnables: HashMap<Fruta, Runnable> =
        HashMap() // map de frutas com runnables pendentes, para que seja possível cancelar

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrutaViewHolder {
        val view = LayoutInflater.from(c).inflate(R.layout.novo_fruta_inflater, parent, false);

        return FrutaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return frutas.size
    }

    override fun onBindViewHolder(holder: FrutaViewHolder, position: Int) {

        val frutaescolhida = frutas[position]
        holder.textViewNome.text = frutaescolhida.nome
        holder.img.setImageResource(frutaescolhida.img)

        if (frutaescolhida.bitten) {
            holder.img.setImageResource(R.drawable.bitten)
        } else {
            holder.img.setImageResource(R.drawable.fruit)
        }

        if (itemsPendingRemoval.contains(frutaescolhida)) {
            //view do swipe/delete
            holder.layoutNormal.setVisibility(View.GONE)
            holder.layoutGone.setVisibility(View.VISIBLE)
            holder.undoButton.setVisibility(View.VISIBLE)
            holder.undoButton.setOnClickListener {
                // usou o undo, remover a pendingRennable
                val pendingRemovalRunnable = pendingRunnables[frutaescolhida]
                Log.i("AULA17", "CLICOU")
                pendingRunnables.remove(frutaescolhida)
                if (pendingRemovalRunnable != null) {
                    handler.removeCallbacks(pendingRemovalRunnable)
                }
                itemsPendingRemoval.remove(frutaescolhida)
                //binda novamente para redesenhar
                notifyItemChanged(frutas.indexOf(frutaescolhida))
            }
        } else {
            //mostra o padrão
            holder.textViewNome.setText(frutaescolhida.nome)
            holder.layoutNormal.setVisibility(View.VISIBLE)
            holder.layoutGone.setVisibility(View.GONE)
            holder.undoButton.setVisibility(View.GONE)
            holder.undoButton.setOnClickListener(null)
            if (frutaescolhida.bitten) {
                holder.img.setImageResource(R.drawable.bitten)
            } else {
                holder.img.setImageResource(R.drawable.fruit)
            }

            holder.img.setOnClickListener{
                frutaescolhida.bitten = true
                notifyItemChanged(position)
            }
        }


    }

    fun remover (position: Int){
        var fruta = frutas[position]

        if (frutas.contains(fruta)){
            frutas.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun removerComTempo(position: Int) {

        val fruta = frutas[position]
        if (!itemsPendingRemoval.contains(fruta)) {
            itemsPendingRemoval.add(fruta)
            notifyItemChanged(position)
            var pendingRemovalRunnable = Runnable {
                remover(position)
            }
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT)
            pendingRunnables[fruta] = pendingRemovalRunnable
        }
    }

    fun mover(fromPosition: Int, toPosition: Int) {

        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                swap(frutas, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                swap(frutas, i, i - 1)
            }
        }

        notifyItemMoved(fromPosition, toPosition)
        notifyItemChanged(toPosition)
        notifyItemChanged(fromPosition)
    }
}