package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.KnowledgeTreeBody

interface KnowledgeTreeContract {
    interface View : IView {
        fun scrollToTop()
        fun setKnowledgeTree(data: List<KnowledgeTreeBody>)
    }

    interface Presenter {
        fun getKnowledgeTree()
    }
}