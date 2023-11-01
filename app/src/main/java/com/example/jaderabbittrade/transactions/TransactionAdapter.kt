package com.example.jaderabbittrade.transactions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jaderabbittrade.Constants
import com.example.jaderabbittrade.R

class TransactionAdapter(context: Context, transactions: MutableList<Transaction>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>()
{
    private var _context: Context
    private var _transactions: MutableList<Transaction>

    init
    {
        _context = context
        _transactions = transactions
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder
    {
        return TransactionViewHolder(LayoutInflater.from(_context).inflate(R.layout.transaction_view, parent, false))
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int)
    {
        val currentTransaction = _transactions[position]

        // Set the information view of the transaction
        val coinLogoID = Constants.coinImageMap[currentTransaction.coinName] ?: R.drawable.logo_btc
        holder.coinNameImageView.setImageResource(coinLogoID)

        holder.amountTextView.text = Constants.formatPrice(currentTransaction.amountInDollars.toDouble())
        holder.tradingTypeTextView.text = currentTransaction.tradingType.name

        val dateTimeMap = currentTransaction.splitDateTime()
        holder.dateTextView.text = dateTimeMap["Date"]
        holder.timeTextView.text = dateTimeMap["Time"]

        holder.statusTextView.text = currentTransaction.status.name
        val statusImageID = when (currentTransaction.status)
        {
            TransactionStatus.SUCCESS -> R.drawable.icon_success
            TransactionStatus.PROCESSING -> R.drawable.icon_processing
            else -> R.drawable.icon_failed
        }
        holder.statusImageView.setImageResource(statusImageID)
    }

    override fun getItemCount(): Int
    {
        return _transactions.size
    }

    inner class TransactionViewHolder(transactionView: View): RecyclerView.ViewHolder(transactionView)
    {
        var coinNameImageView: ImageView
        var dateTextView: TextView
        var timeTextView: TextView
        var amountTextView: TextView
        var tradingTypeTextView: TextView
        var statusTextView: TextView
        var statusImageView: ImageView

        init
        {
            coinNameImageView = transactionView.findViewById(R.id.coin_name_image_view)
            dateTextView = transactionView.findViewById(R.id.date_text_view)
            timeTextView = transactionView.findViewById(R.id.time_text_view)
            amountTextView = transactionView.findViewById(R.id.amount_text_view)
            tradingTypeTextView = transactionView.findViewById(R.id.trading_type_text_view)
            statusTextView = transactionView.findViewById(R.id.status_text_view)
            statusImageView = transactionView.findViewById(R.id.status_image_view)
        }
    }
}