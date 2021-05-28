package com.societe.generale.bank.account.model;


import org.junit.Assert;
import org.junit.Test;

import model.Client;

public class ClientTest {

	@Test
	public void shouldNotBeNullWhenInstanciated() {
		Client client  = new Client();
		Assert.assertNotNull(client);
	}

}
