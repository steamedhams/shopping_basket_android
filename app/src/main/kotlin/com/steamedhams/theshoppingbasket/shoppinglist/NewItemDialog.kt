package com.steamedhams.theshoppingbasket.shoppinglist

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.steamedhams.theshoppingbasket.R

/**
 * Dialog class providing a dialog from which users can create new text based shopping list Items
 * <p>
 * Created by richard on 12/02/17.
 */
class NewItemDialog(val callback : (String) -> Unit) : DialogFragment() {

    @BindView(R.id.dialog_cancel_button)
    lateinit var dialogCancelButton : TextView

    @BindView(R.id.dialog_add_button)
    lateinit var dialogAddButton : TextView

    @BindView(R.id.new_list_item_edit_text)
    lateinit var newListItemEditText : EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view : View = inflater.inflate(R.layout.new_list_item_dialog, null)

        ButterKnife.bind(this, view)

        dialog.setCanceledOnTouchOutside(true)

        dialogCancelButton.setOnClickListener { dialog.cancel() }
        dialogAddButton.setOnClickListener {
            val text : String = newListItemEditText.text.toString()
            if (text.isNotEmpty()) {
                callback(text)
            }
            dismiss()
        }

        newListItemEditText.setOnEditorActionListener { textView, _, _ ->
            callback(textView.text.toString())
            dismiss()
            true
        }

        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        return view
    }

    override fun onResume() {
        super.onResume()
        newListItemEditText.post { newListItemEditText.requestFocus() }
    }

    companion object Factory {
        fun getTag() : String? {
            return NewItemDialog::class.java.simpleName
        }
    }
}