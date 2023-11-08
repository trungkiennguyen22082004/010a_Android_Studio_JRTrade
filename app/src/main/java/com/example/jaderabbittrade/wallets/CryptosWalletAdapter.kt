package com.example.jaderabbittrade.wallets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jaderabbittrade.Constants
import com.example.jaderabbittrade.R

class CryptosWalletAdapter(context: Context, cryptosWallet: MutableList<CryptoBalance>) : RecyclerView.Adapter<CryptosWalletAdapter.CryptoBalanceViewHolder>()
{
    private var _context: Context
    private var _cryptosBalance: MutableList<CryptoBalance>

    var onItemClickListener : ((CryptoBalance) -> Unit)? = null

    init
    {
        _context = context
        _cryptosBalance = cryptosWallet
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptosWalletAdapter.CryptoBalanceViewHolder
    {
        return CryptoBalanceViewHolder(LayoutInflater.from(_context).inflate(R.layout.view_crypto_balance, parent, false))
    }

    override fun onBindViewHolder(holder: CryptosWalletAdapter.CryptoBalanceViewHolder, position: Int)
    {
        val currentCryptoBalance = _cryptosBalance[position]

        // Set the information view of the transaction
        val coinLogoID = Constants.coinImageMap[currentCryptoBalance.coinCode] ?: R.drawable.logo_btc
        holder.coinNameImageView.setImageResource(coinLogoID)

        holder.coinCodeTextView.text = currentCryptoBalance.coinCode
        holder.coinAmountTextView.text = String.format("%.2f", currentCryptoBalance.amount)

        holder.itemView.setOnClickListener()
        {
            this.onItemClickListener?.invoke(currentCryptoBalance)
        }
    }

    override fun getItemCount(): Int
    {
        return _cryptosBalance.size
    }

    inner class CryptoBalanceViewHolder(cryptoBalanceView: View) : RecyclerView.ViewHolder(cryptoBalanceView)
    {
        var coinNameImageView: ImageView
        var coinCodeTextView: TextView
        var coinAmountTextView: TextView

        init
        {
            coinNameImageView = cryptoBalanceView.findViewById(R.id.coin_name_image_view)
            coinCodeTextView = cryptoBalanceView.findViewById(R.id.coin_code)
            coinAmountTextView = cryptoBalanceView.findViewById(R.id.coin_amount)
        }
    }
}