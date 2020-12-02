package com.theoparis.creepinoutils.util.client.obj

import net.minecraftforge.eventbus.api.Event

open class TessellatorModelEvent(var model: ObjModel?) : Event() {
    class RenderPre(model: ObjModel?) : TessellatorModelEvent(model)
    class RenderPost(model: ObjModel?) : TessellatorModelEvent(model)
    open class RenderGroupEvent(var group: String?, model: ObjModel?) : TessellatorModelEvent(model) {
        class Pre(g: String?, m: ObjModel?) : RenderGroupEvent(g, m)
        class Post(g: String?, m: ObjModel?) : RenderGroupEvent(g, m)
    }

    override fun isCancelable(): Boolean {
        return true
    }
}