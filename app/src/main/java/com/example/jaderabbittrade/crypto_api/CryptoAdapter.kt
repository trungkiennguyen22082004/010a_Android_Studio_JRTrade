package com.example.jaderabbittrade.crypto_api

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.jaderabbittrade.Constants
import com.example.jaderabbittrade.R

class CryptoAdapter(context: Context, cryptoAssets: MutableList<CryptoCurrency>) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>()
{
    private var _context: Context
    private var _initialCryptoAssets: MutableList<CryptoCurrency>
    private var _currentCryptoAssets: MutableList<CryptoCurrency>

    init
    {
        _context = context
        _initialCryptoAssets = cryptoAssets
        _currentCryptoAssets = cryptoAssets
    }

    var onItemClickListener : ((CryptoCurrency) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoAdapter.CryptoViewHolder
    {
        return CryptoViewHolder(LayoutInflater.from(_context).inflate(R.layout.view_crypto_asset, parent, false))
    }

    override fun onBindViewHolder(holder: CryptoAdapter.CryptoViewHolder, position: Int)
    {
        val currentCryptoAsset = _currentCryptoAssets[position]

        val coinLogoID = Constants.coinImageMap[currentCryptoAsset.symbol] ?: R.drawable.logo_btc
        holder.coinNameImageView.setImageResource(coinLogoID)

        holder.coinNameTextView.text = currentCryptoAsset.name
        holder.coinCodeTextView.text = currentCryptoAsset.symbol

        val percentChange24h = currentCryptoAsset.quotes[0].percentChange24h
        holder.coinChangeTextView.text = String.format("%.2f", percentChange24h)
        if (percentChange24h >= 0)
        {
            holder.coinChangeTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        }
        else
        {
            holder.coinChangeTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        }

        holder.coinPriceTextView.text =  Constants.formatPrice(currentCryptoAsset.quotes[0].price)

        holder.itemView.setOnClickListener()
        {
            this.onItemClickListener?.invoke(currentCryptoAsset)
        }
    }

    override fun getItemCount(): Int
    {
        return _currentCryptoAssets.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun search(query: String, sortedQuery: String)
    {
        val searchedCryptoAssets: MutableList<CryptoCurrency> = mutableListOf()

        if (query.isEmpty())
        {
            searchedCryptoAssets.addAll(_initialCryptoAssets)
            Log.d("Searching", searchedCryptoAssets.size.toString())
        }
        else
        {
            val lowercaseQuery = query.lowercase()
            for (cryptoAsset in _currentCryptoAssets)
            {
                if (cryptoAsset.name.lowercase().contains(lowercaseQuery) || cryptoAsset.symbol.lowercase().contains(lowercaseQuery))
                {
                    searchedCryptoAssets.add(cryptoAsset)
                }
            }
        }

        _currentCryptoAssets = searchedCryptoAssets
        this.sort(sortedQuery)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sort(query: String)
    {
        when (query)
        {
            "Hot" ->
            {
                _currentCryptoAssets = _currentCryptoAssets.sortedBy { it.cmcRank } as MutableList<CryptoCurrency>
            }
            "Price" ->
            {
                _currentCryptoAssets = _currentCryptoAssets.sortedByDescending { it.quotes.firstOrNull()?.price ?: 0.0 } as MutableList<CryptoCurrency>
            }
            "24h Change" ->
            {
                _currentCryptoAssets = _currentCryptoAssets.sortedByDescending { it.quotes.firstOrNull()?.percentChange24h ?: 0.0 } as MutableList<CryptoCurrency>
            }
        }

        Log.d("Sorting", "Sorted by $query, output: $_currentCryptoAssets")
        notifyDataSetChanged()
    }

    inner class CryptoViewHolder(cryptoAssetView: View): RecyclerView.ViewHolder(cryptoAssetView)
    {
        var coinNameImageView: ImageView
        val coinNameTextView: TextView
        val coinCodeTextView: TextView
        val coinChangeTextView: TextView
        val coinPriceTextView: TextView

        init
        {
            coinNameImageView = cryptoAssetView.findViewById(R.id.coin_name_image_view)
            coinNameTextView = cryptoAssetView.findViewById(R.id.coin_name)
            coinCodeTextView = cryptoAssetView.findViewById(R.id.coin_code)
            coinChangeTextView = cryptoAssetView.findViewById(R.id.coin_change)
            coinPriceTextView = cryptoAssetView.findViewById(R.id.coin_price)
        }
    }
}