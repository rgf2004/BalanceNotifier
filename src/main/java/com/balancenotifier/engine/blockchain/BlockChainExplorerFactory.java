package com.balancenotifier.engine.blockchain;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balancenotifier.telegram.handler.WalletType;

@Service
public class BlockChainExplorerFactory {

	@Autowired
	private Map<String, BlockChainExplorer> explorers;
	
	
	public BlockChainExplorer getExplorer(WalletType walletType)
	{
		switch (walletType){
		
		case ENT:
			return explorers.get("eternityBlockChainExplorer");
		
		case ARC:
			return explorers.get("arcticBlockChainExplorer");
		
		};
		
		return null;
	}
	
}
