# BukaBuku

The customer and admin Swing applications share the MySQL database configured in `docker-compose.yml`.

## Run locally

Requirements: Java 11 or newer, Maven, and Docker Desktop.

1. Start MySQL:

   ```powershell
   docker compose up -d --wait
   ```

2. Start the customer application:

   ```powershell
   mvn compile exec:java -Dexec.mainClass=BukaBukuApp
   ```

3. In a separate terminal, start the admin dashboard:

   ```powershell
   mvn compile exec:java -Dexec.mainClass=AdminDashboardApp
   ```

The applications create the database tables and seed the catalog on first startup. Customer accounts are created from the registration screen. New database settings can be supplied with `DB_URL`, `DB_USER`, and `DB_PASSWORD`; defaults match the local Compose service.

Stop MySQL with `docker compose down`. The `mysql_data` volume preserves catalog changes, accounts, and orders.
