package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.ProjectTreeBody

interface ProjectTreeContract {
    interface View:IView{
        fun scrollTop()
       fun setProjectTree(data:List<ProjectTreeBody>)
    }

    interface Presenter{
        fun getProjectTree()
    }

}