package net.mabako.zwickau.mensa;

import net.robotmedia.billing.BillingController;

public class BillingConfiguration implements BillingController.IConfiguration {

	public byte[] getObfuscationSalt() {
		return new byte[] { -82, -119, -109, -61, -56, 86, 104, 87, -33, -77,
				12, -65, -97, 57, 68, -45, -83, 52, 119, -29 };
	}

	public String getPublicKey() {
		return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuIqEKvsVjPW72zH00QW60KgzWdNdX/9ejX1Zdd2pL9GhOcsgPD+c7Z6WT8l22qsMFK33XYQ/MbSi0LJRQvhPZhd2wKOSCrb3PGfn69HUfr3LheJrPVG6CDuSm1lOqlxPJgu4JmJX2Jud5XE5iY5Ar0MwBoSB6uaMtX94OasqcneUvG9r7RkRXCpGZcfF0C4iE0Od47wmuQm35gy2M7GEs2hxaWC8cxfyniRxjMSMyoWQjlka7ZfIRip5SPMsXklU1qRV1g+InZA5AwIAsB8vcx4i+BJpDgqriYesGH9WY4E+MZe64ailHvnf8HAzAYrLErOLisEjkRCoNsAR+RPNDwIDAQAB";
	}
}
