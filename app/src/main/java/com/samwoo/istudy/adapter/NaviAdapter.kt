package com.samwoo.istudy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.samwoo.istudy.R
import com.samwoo.istudy.activity.ContentActivity
import com.samwoo.istudy.bean.Article
import com.samwoo.istudy.bean.NavigationBean
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.util.randomColor
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.item_navigation_list.view.*
import org.jetbrains.anko.intentFor

class NaviAdapter(val context: Context?, val datas: MutableList<NavigationBean>) :
    BaseQuickAdapter<NavigationBean, BaseViewHolder>(R.layout.item_navigation_list, datas) {
    override fun convert(helper: BaseViewHolder?, item: NavigationBean?) {
        item ?: return
        helper ?: return
        helper.setText(R.id.nav_title, item.name)
        val articles: List<Article> = item.articles
        val tagLayout = helper.getView<TagFlowLayout>(R.id.nav_flow_layout)
        tagLayout.run {
            adapter = object : TagAdapter<Article>(articles) {
                override fun getView(parent: FlowLayout?, position: Int, t: Article?): View {
                    val tv = LayoutInflater.from(context).inflate(
                        R.layout.flow_layout_tv,
                        nav_flow_layout,
                        false
                    ) as TextView
                    tv.apply {
                        text = articles[position].title
                        setTextColor(resources.getColor(R.color.white))
//                        background = resources.getDrawable(R.drawable.tag_flow_bg)
                        setBackgroundColor(randomColor())
                    }

                    return tv
                }
            }
            setOnTagClickListener { _, position, _ ->
                if (articles.isNotEmpty()) {
                    val data = articles[position]
                    val intent = context.intentFor<ContentActivity>(
                        Pair(Constant.CONTENT_URL_KEY, data.link),
                        Pair(Constant.CONTENT_TITLE_KEY, data.title),
                        Pair(Constant.CONTENT_ID_KEY, data.id)
                    )
                    context.startActivity(intent)

                }
                false
            }
        }
    }


}