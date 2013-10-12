package com.andevcon.playtag;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.andevcon.playtag.util.HexUtil;

public class MainActivity extends Activity {

	private NfcAdapter nfcAdapter;
	private PendingIntent nfcPendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Grab the NFC Adapter found on the phone.
		// TODO: If returns null, NFC is disabled or not available
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		// Setup a PendingIntent to trigger when an NFC scan occurs
		Intent intent = new Intent(this, getClass());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		nfcPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String tagId = HexUtil.bytesToHex(tag.getId());
			Log.i("MainActivity",
					String.format("Found NFC Tag with serial number %s", tagId));
			onNfcTagDiscovered(tag, intent);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (null != nfcAdapter) {
			nfcAdapter.disableForegroundDispatch(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (null != nfcAdapter) {
			nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, null,
					null);
		}
	}

	private void onNfcTagDiscovered(Tag tag, Intent intent) {
		// Do something cool here
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
