/*
 * Originally created by apemanzilla
 * Found at https://github.com/apemanzilla/KWallet
 */

package io.github.apemanzilla.kwallet;

import net.teamdentro.kristwallet.exception.BadValueException;
import net.teamdentro.kristwallet.exception.InsufficientFundsException;
import net.teamdentro.kristwallet.exception.InvalidRecipientException;
import net.teamdentro.kristwallet.exception.SelfSendException;
import net.teamdentro.kristwallet.exception.UnknownException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import io.github.apemanzilla.kwallet.types.Address;
import io.github.apemanzilla.kwallet.types.Transaction;
import io.github.apemanzilla.kwallet.util.HTTP;

public class KristAPI {
    private URL remoteAPI;
    private String key;
    private String address;

    public KristAPI(URL remoteAPI, String privateKey) {
        this.remoteAPI = remoteAPI;
        this.key = privateKey;
        this.address = makeAddressV2(privateKey);
    }

    public long getBalance() throws NumberFormatException, IOException {
        return getBalance(address);
    }

    public long getBalance(String address) throws NumberFormatException, IOException {
        return Long.parseLong(HTTP.readURL(new URL(remoteAPI, "?getbalance=" + address)));
    }

    public Transaction[] getTransactions(String address) throws IOException {
        String transactionData = HTTP.readURL(new URL(remoteAPI, "?listtx=" + address));
        transactionData = transactionData.substring(0, transactionData.length() - 3).replace("\n", "").replace("\r", "");
        if (transactionData.length() == 0) {
            return new Transaction[0];
        } else if ((transactionData.length() % 31) == 0) {
            Transaction[] transactions = new Transaction[transactionData.length() / 31];
            for (int i = 0; i < transactionData.length() / 31; i++) {
                transactions[i] = new Transaction(transactionData.substring(i * 31, (i + 1) * 31), address);
            }
            return transactions;
        } else {
            return new Transaction[0];
        }
    }

    public Transaction[] getTransactions() throws IOException {
        return getTransactions(address);
    }

    public Address[] getRichList() throws IOException, ParseException {
        // Extra HTML tags seem to get caught without this regex
        String richList = HTTP.readURL(new URL(remoteAPI, "?richapi")).replaceAll("<[^>]*>", "");
        if (richList.length() == 0) {
            return new Address[0];
        } else if ((richList.length() % 29) == 0) {
            Address[] result = new Address[richList.length() / 29];
            for (int i = 0; i < richList.length() / 29; i++) {
                result[i] = new Address(richList.substring(i * 29, (i + 1) * 29));

            }
            return result;
        } else {
            return new Address[0];
        }
    }

    public enum TransferResults {
        Success,
        InsufficientFunds,
        NotEnoughKST,
        BadValue,
        InvalidRecipient,
        SelfSend,
        Unknown
    }

    public void sendKrist(long amount, String recipient) throws IOException, SelfSendException, InsufficientFundsException, BadValueException, InvalidRecipientException, UnknownException {
        if (address.equals(recipient))
            throw new SelfSendException();
        switch (HTTP.readURL(new URL(remoteAPI, "?pushtx2&q=" + recipient + "&pkey=" + key + "&amt=" + amount))) {
            case "Success": {
                return;
            }
            case "Error1": {
                throw new InsufficientFundsException();
            }
            case "Error2": {
                throw new InsufficientFundsException();
            }
            case "Error3": {
                throw new BadValueException();
            }
            case "Error4": {
                throw new InvalidRecipientException();
            }
            default: {
                throw new UnknownException();
            }
        }
    }

    private char numtochar(int inp) {
        for (int i = 6; i <= 251; i += 7) {
            if (inp <= i) {
                if (i <= 69) {
                    return (char) ('0' + (i - 6) / 7);
                }
                return (char) ('a' + ((i - 76) / 7));
            }
        }
        return 'e';
    }

    private String makeAddressV2(String key) {
        String[] protein = {"", "", "", "", "", "", "", "", ""};
        int link;
        String v2 = "k";
        String stick = sha256Hex(sha256Hex(key));
        for (int i = 0; i <= 9; i++) {
            if (i < 9) {
                protein[i] = stick.substring(0, 2);
                stick = sha256Hex(sha256Hex(stick));
            }
        }
        int i = 0;
        while (i <= 8) {
            link = Integer.parseInt(stick.substring(2 * i, 2 + (2 * i)), 16) % 9;
            if (protein[link].equals("")) {
                stick = sha256Hex(stick);
            } else {
                v2 = v2 + numtochar(Integer.parseInt(protein[link], 16));
                protein[link] = "";
                i++;
            }
        }
        return v2;
    }

    public static String sha256Hex(String in) {
        return new String(Hex.encodeHex(DigestUtils.sha256(in)));
    }

    public String getAddress() {
        return address;
    }
}