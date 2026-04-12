import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theapp.FriendSection
import com.example.theapp.FriendsAdapter
import com.example.theapp.ListItem
import com.example.theapp.R
import com.google.android.material.divider.MaterialDividerItemDecoration

class OuterFriendsAdapter(
    private val sections: List<FriendSection>
) : RecyclerView.Adapter<OuterFriendsAdapter.SectionVH>() {

    class SectionVH(view: View) : RecyclerView.ViewHolder(view) {
        val headerText: TextView = view.findViewById(R.id.header_text)
        val innerRecycler: RecyclerView = view.findViewById(R.id.innerRecyclerView)

        val adapter = FriendsAdapter()
        val materialDivider = MaterialDividerItemDecoration(view.context, LinearLayoutManager.VERTICAL).apply {
            dividerColor = ContextCompat.getColor(view.context, R.color.divider_color)
            dividerThickness = 2
            isLastItemDecorated = false
        }

        init {
            innerRecycler.layoutManager = LinearLayoutManager(view.context)
            innerRecycler.adapter = adapter
            innerRecycler.addItemDecoration(materialDivider)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.outer_item, parent, false)
        return SectionVH(view)
    }

    override fun onBindViewHolder(holder: SectionVH, position: Int) {
        val section = sections[position]

        holder.headerText.text = section.header.toString()
        holder.adapter.submitList(
            section.friends.map { ListItem.FriendItem(it) }
        )
    }

    override fun getItemCount() = sections.size
}