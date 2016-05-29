package com.example.oscar.travelagent2;


import android.provider.ContactsContract;

public final class Contents {
    private Contents() {
    }

    public static final class Type {
        public static final String CONTACT = "CONTACT_TYPE";
        public static final String LOCATION = "LOCATION_TYPE";

        private Type() {
        }
        public static final String TEXT = "TEXT_TYPE";

        // An email type. Use Intent.putExtra(DATA, string) where string is the email address.
        public static final String EMAIL = "EMAIL_TYPE";

        // Use Intent.putExtra(DATA, string) where string is the phone number to call.
        public static final String PHONE = "PHONE_TYPE";

        // An SMS type. Use Intent.putExtra(DATA, string) where string is the number to SMS.
        public static final String SMS = "SMS_TYPE";
    }

    public static final String URL_KEY = "URL_KEY";

    public static final String NOTE_KEY = "NOTE_KEY";

    public static final String[] PHONE_KEYS = {
            ContactsContract.Intents.Insert.PHONE, ContactsContract.Intents.Insert.SECONDARY_PHONE,
            ContactsContract.Intents.Insert.TERTIARY_PHONE
    };

    public static final String[] PHONE_TYPE_KEYS = {
            ContactsContract.Intents.Insert.PHONE_TYPE,
            ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE,
            ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE
    };

    public static final String[] EMAIL_KEYS = {
            ContactsContract.Intents.Insert.EMAIL, ContactsContract.Intents.Insert.SECONDARY_EMAIL,
            ContactsContract.Intents.Insert.TERTIARY_EMAIL
    };

    public static final String[] EMAIL_TYPE_KEYS = {
            ContactsContract.Intents.Insert.EMAIL_TYPE,
            ContactsContract.Intents.Insert.SECONDARY_EMAIL_TYPE,
            ContactsContract.Intents.Insert.TERTIARY_EMAIL_TYPE
    };
}

