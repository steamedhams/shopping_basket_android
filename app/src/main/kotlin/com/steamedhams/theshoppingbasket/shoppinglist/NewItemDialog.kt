package com.steamedhams.theshoppingbasket.shoppinglist

import android.app.DialogFragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.steamedhams.theshoppingbasket.R
import com.steamedhams.theshoppingbasket.databinding.NewListItemDialogBinding

/**
 * Dialog class providing a dialog from which users can create new text based shopping list Items
 * <p>
 * Created by richard on 12/02/17.
 */
class NewItemDialog(val callback : (String) -> Unit) : DialogFragment() {

    private lateinit var binding : NewListItemDialogBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate<NewListItemDialogBinding>(inflater, R.layout.new_list_item_dialog, container, false)

        dialog.setCanceledOnTouchOutside(true)

        binding.dialogCancelButton.setOnClickListener { dialog.cancel() }
        binding.dialogAddButton.setOnClickListener {
            val text : String = binding.newListItemEditText.text.toString()
            if (text.isNotEmpty()) {
                callback(text)
            }
            dismiss()
        }

        binding.newListItemEditText.setOnEditorActionListener { textView, _, _ ->
            callback(textView.text.toString())
            dismiss()
            true
        }

        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.newListItemEditText.post {  binding.newListItemEditText.requestFocus() }
    }

    companion object Factory {
        fun getTag() : String? {
            return NewItemDialog::class.java.simpleName
        }
    }
}