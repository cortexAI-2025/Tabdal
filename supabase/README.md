# Backend BALAL (Supabase)

- Projet : **BALAL IMMO** — ref `ovnlxcxbnfvvmgeltetg`, région `eu-west-3` (Paris)
- URL : https://ovnlxcxbnfvvmgeltetg.supabase.co
- Clé publique (publishable, sûre côté app) : dans `app/build.gradle.kts` (SUPABASE_ANON_KEY)
- Ne JAMAIS mettre la clé `service_role` dans l'app ou le dépôt.

## Schéma (appliqué)
`profiles` (public, sans téléphone) · `profile_private` (téléphone, propriétaire seul) · `listings` ·
`favorites` · `conversations` · `messages` (temps réel) · `reports` · bucket Storage `listing-photos`.
RLS activée partout ; rôle `ADMIN` non modifiable par l'utilisateur.

Les migrations appliquées sont archivées dans `migrations/` (le détail fait foi dans Supabase > Database > Migrations).
