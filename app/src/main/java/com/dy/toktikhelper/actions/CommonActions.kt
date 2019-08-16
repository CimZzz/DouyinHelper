package com.dy.toktikhelper.actions

import com.dy.toktikhelper.core.BaseAction
import com.dy.toktikhelper.core.BaseMessage

object SocketInitAction: BaseAction()
object SocketDestroyAction: BaseAction()
object SocketInitErrorAction: BaseAction()

class SocketOutAction(baseMessage: BaseMessage): BaseAction()