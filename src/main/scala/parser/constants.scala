package com.wildpants.wub
package parser

import console.Style
import console.Style.*

/* ---------------- Default Styles ---------------- */

inline def FAILURE_STYLE = Red + Bold
inline def SUCCESS_STYLE = Green
inline def TASK_STYLE = Cyan + Bold + Style(s => s"'$s'")
inline def HEADER_STYLE = Bold + Underline


/* ---------------- Default elements ---------------- */

inline def ERROR_TAG = "error:" <<< FAILURE_STYLE
inline def OK_TAG = "ok:" <<< SUCCESS_STYLE
