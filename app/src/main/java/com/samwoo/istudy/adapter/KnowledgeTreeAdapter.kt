package com.samwoo.istudy.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.samwoo.istudy.R
import com.samwoo.istudy.bean.Knowledge
import com.samwoo.istudy.bean.KnowledgeTreeBody
import com.samwoo.istudy.util.DisplayManager.dp2px
import com.samwoo.istudy.util.randomColor
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.item_knowledge_tree_list.view.*


class KnowledgeTreeAdapter(private val context: Context?, datas: MutableList<KnowledgeTreeBody>) :
    BaseQuickAdapter<KnowledgeTreeBody, BaseViewHolder>(
        R.layout.item_knowledge_tree_list, datas
    ) {
    override fun convert(helper: BaseViewHolder?, item: KnowledgeTreeBody?) {
        item ?: return
        helper ?: return
        helper.setText(R.id.title_first, item?.name)
        val knowledge = item?.children
        val tagLayout = helper.getView<TagFlowLayout>(R.id.knowledage_flow_layout) as TagFlowLayout
        tagLayout.run {
            adapter = object : TagAdapter<Knowledge>(knowledge) {
                override fun getView(parent: FlowLayout?, position: Int, t: Knowledge?): View {
                    val tv = LayoutInflater.from(context).inflate(
                        R.layout.flow_layout_tv,
                        knowledage_flow_layout,
                        false
                    ) as TextView

                    tv.apply {
                        text = knowledge[position].name
                        setTextColor(randomColor())
//                        setTextColor(ContextCompat.getColor(context, R.color.white))
                        background = getBackGround(context)
                        setPadding(dp2px(4))
                    }
                    return tv
                }
            }
        }
//        item?.children.let {
//            helper?.setText(R.id.title_second, it?.joinToString(" / ", transform = { child ->
//                child.name
//            }))
//        }
    }

    /**
     * 随机获取标签背景颜色
     */
    private fun getBackGround(context: Context): Drawable {
//        val colors = intArrayOf(randomColor(), randomColor())
        var drawable = GradientDrawable()
        drawable.apply {
//            orientation = GradientDrawable.Orientation.LEFT_RIGHT
            cornerRadius = 8f
//            gradientType = GradientDrawable.LINEAR_GRADIENT
//            setColors(colors)
            setColor(ContextCompat.getColor(context, R.color.Grey300))
        }
        return drawable
    }
}