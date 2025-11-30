package com.example.moneyfy.ui.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FormItem(
    label: String,
    value: String,
    icon: ImageVector,
    onClick: (() -> Unit)? = null,
    isAmount: Boolean = false,
    onValueChange: ((String) -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFF0D47A1), RoundedCornerShape(12.dp))
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            if (isAmount && onValueChange != null) {
                BasicTextField(
                    value = value,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) onValueChange(newValue)
                    },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.White, fontSize = 17.sp),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    decorationBox = { innerTextField ->
                        if (value.isEmpty()) {
                            Text("VNĐ", color = Color.White.copy(alpha = 0.5f), fontSize = 17.sp)
                        }
                        innerTextField()
                    }
                )
            } else {
                Column(Modifier.weight(1f)) {
                    Text(label, color = Color.White, fontSize = 13.sp)
                    Text(value, color = Color.White, fontSize = 17.sp)
                }
            }

            if (!isAmount) {
                Text(">", color = Color.White, fontSize = 20.sp)
            }
        }
    }
}
