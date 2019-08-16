package com.dy.toktikhelper.messages

import com.dy.toktikhelper.core.BaseMessage
import com.dy.toktikhelper.core.Constants

class UnsupportedApiMessage: BaseMessage() {
    init {
        messageType = Constants.MessageType.Unsupported
    }
}