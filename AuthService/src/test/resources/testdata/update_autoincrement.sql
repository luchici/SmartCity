SELECT setval('account_account_id_seq', (SELECT MAX(account_id) from "account"));
