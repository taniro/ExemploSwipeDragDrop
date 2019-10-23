package tads.eaj.ufrn.exemploswipedragdrop

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper.*








class MainActivity : AppCompatActivity() {

    val db: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "frutas.sqlite")
            .allowMainThreadQueries()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preparabanco()

        var listaFrutas:MutableList<Fruta> = db.frutaDao().listAll()

        var adapter = FrutaAdapter(this, listaFrutas)
        recyclerview.adapter = adapter

        val layout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerview.layoutManager = layout


        /*

        EXEMPLO 1

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

                Log.i(
                    "AULA17",
                    "Drag flags: " + Integer.toBinaryString(dragFlags) + "Swipe flags: " + Integer.toBinaryString(
                        swipeFlags
                    )
                ) //11 e 110000
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(recyclerView: RecyclerView, dragged: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                Log.i(
                    "AULA17",
                    "OnMove invocado. Mover da posição " + dragged.adapterPosition + " para " + target.adapterPosition
                )
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.i("AULA17", "OnSwipe invocado. Direção: " + Integer.toBinaryString(direction))

            }
        })

         */

        /*

        EXEMPLO 2

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            UP or DOWN, START or END  )
        {

            override fun onMove( recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder): Boolean {
                Log.i("AULA17", "OnMove")
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.i("AULA17", "OnSwiped")
            }

        })
         */


        //EXEMPLO 3

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            UP or DOWN, START or END  )
        {

            override fun onMove( recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder): Boolean {
                Log.i("AULA17", "OnMove")
                //é usado para operações drag and drop
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                val adapter = recyclerView.adapter as FrutaAdapter
                adapter.mover(fromPosition, toPosition)
                return true// true se moveu, se não moveu, retorne falso
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var posicao = viewHolder.adapterPosition
                var adapter = recyclerview.adapter as FrutaAdapter

                //adapter.remover(posicao)
                adapter.removerComTempo(posicao)

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val background = ColorDrawable(resources.getColor(R.color.red))
                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.adapterPosition === -1) {
                    // not interested in those
                    return
                }
                Log.i("AULA17", "dx = $dX")
                // Here, if dX > 0 then swiping right.
                // If dX < 0 then swiping left.
                // If dX == 0 then at at start position.
                // draw red background
                if (dX < 0) {
                    Log.i("AULA17", "dX < 0")
                    background.setBounds(
                        (itemView.right + dX).toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                } else if (dX > 0) {
                    Log.i("AULA17", "dX > 0")
                    background.setBounds(
                        itemView.left,
                        itemView.top,
                        (dX).toInt(),
                        itemView.bottom
                    )
                }
                background.draw(c)

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

            }

            override fun isLongPressDragEnabled(): Boolean {
                //return false; se quiser, é possivel desabilitar o drag and drop
                return true
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                //return false; se quiser, é possivel desabilitar o swipe
                return true
            }

        })

        itemTouchHelper.attachToRecyclerView(recyclerview)

    }

    fun preparabanco(){
        db.frutaDao().deleteAll()

        val f1 = Fruta ("Pera", R.drawable.fruit)
        val f2 = Fruta ("Uva", R.drawable.fruit)
        val f3 = Fruta ("Goiaba", R.drawable.fruit)
        val f4 = Fruta ("Maça", R.drawable.fruit)
        val f5 = Fruta ("Tomate", R.drawable.fruit)
        val f6 = Fruta ("Banana", R.drawable.fruit)
        val f7 = Fruta ("Caju", R.drawable.fruit)
        val f8 = Fruta ("Maracuja", R.drawable.fruit)
        val f9 = Fruta ("Melancia", R.drawable.fruit)
        val f10 = Fruta ("Melão", R.drawable.fruit)
        val f11 = Fruta ("Mamão", R.drawable.fruit)
        val f12 = Fruta ("Tamarindo", R.drawable.fruit)
        val f13 = Fruta ("Acerola", R.drawable.fruit)
        val f14 = Fruta ("Jambo", R.drawable.fruit)
        val f15 = Fruta ("Graviola", R.drawable.fruit)
        val f16 = Fruta ("Pinha", R.drawable.fruit)

        db.frutaDao().inserirAll(
            f1,
            f2,
            f3,
            f4,
            f5,
            f6,
            f7,
            f8,
            f9,
            f10,
            f11,
            f12,
            f13,
            f14,
            f15,
            f16
        )


    }
}
