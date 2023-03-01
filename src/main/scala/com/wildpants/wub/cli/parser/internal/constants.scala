package com.wildpants.wub.cli
package parser
package internal

import console.Style
import console.Style.*

// Some contant like element default styles HERE

private[parser] val APP_NAME_STYLE: Style = Bold
private[parser] val CMD_NAME_STYLE: Style = Bold + Cyan
private[parser] val HEADER_STYLE: Style = Bold + Underline
private[parser] val REQ_ARG_STYLE: Style = Yellow
private[parser] val OPT_ARG_COLOR: Style = Green
