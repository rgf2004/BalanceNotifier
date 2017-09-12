package com.balancenotifier.engine.blockchain.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.balancenotifier.engine.blockchain.BlockChainExplorer;

@Component
@Qualifier("Eternity") // TODO Qualifier doesn't work need more investigation
public class EternityBlockChainExplorer implements BlockChainExplorer {

	private static final Logger logger = LoggerFactory.getLogger(EternityBlockChainExplorer.class);

	private final String URL = "https://chainz.cryptoid.info/ent/api.dws?q={action}&key={key}&a={address}";

	@Override
	public double getBalance(String address) {

		RestTemplate restTemplate = new RestTemplate();
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.add("User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
			headers.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			headers.add("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
			headers.add("Accept-Encoding", "none");
			headers.add("Accept-Language", "en-US,en;q=0.8");
			headers.add("Connection", "keep-alive");

			HttpEntity<String> request = new HttpEntity<String>(headers);

			ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, request, String.class,
					"getbalance", "38b443860f06", address);

			return Double.parseDouble(response.getBody());

		} catch (Exception ex) {

			logger.error("Error Occured While Getting balance of address [" + address + "]", ex);

			return 0;
		}

	}

}
