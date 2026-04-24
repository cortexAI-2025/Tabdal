# TABDAL – immobilier Maroc 🏠

Application Android native pour la mise en relation immobilière au Maroc (MVP V1).

---

## Stack technique

| Composant | Technologie |
|---|---|
| Langage | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Network | Retrofit + OkHttp + Moshi |
| Base de données locale | Room |
| Authentification | Firebase Auth (email + SMS) |
| Stockage images | Firebase Storage |
| Notifications | Firebase Cloud Messaging (FCM) |
| Géolocalisation | Google Maps Compose |
| Pagination | Paging 3 |
| Images | Coil |

---

## Structure du projet

```
com.tabdal.android/
├── data/
│   ├── local/          → Room (entities, DAOs, Database)
│   ├── remote/         → Retrofit (API, DTOs, interceptors)
│   └── repository/     → Implémentations des repositories
├── domain/
│   ├── models/         → Entités métier
│   ├── repository/     → Interfaces des repositories
│   └── usecases/       → Cas d'usage (auth, listing, message)
├── presentation/
│   ├── navigation/     → NavGraph + Screen sealed class
│   ├── theme/          → Couleurs, typographie, thème Material 3
│   ├── ui/             → Écrans Compose (auth, home, listing, messaging, profile)
│   └── viewmodels/     → ViewModels Hilt
├── di/                 → Modules Hilt (Network, Database, Firebase, Repository)
└── utils/              → Result, Constants, Extensions, FCM Service
```

---

## Fonctionnalités V1

- ✅ Inscription email + vérification SMS (Firebase Phone Auth)
- ✅ Connexion / déconnexion
- ✅ Réinitialisation mot de passe
- ✅ Modification profil (nom, téléphone, ville)
- ✅ Suppression de compte + anonymisation 30 jours
- ✅ Export données utilisateur (JSON)
- ✅ Création/modification/suppression d'annonces
- ✅ Jusqu'à 10 photos par annonce
- ✅ Recherche + filtres (ville, type, prix, surface)
- ✅ Pagination (20 annonces/page)
- ✅ Tri (récent, prix croissant/décroissant)
- ✅ Détail annonce avec carousel photos + carte
- ✅ Système de favoris
- ✅ Messagerie interne (fil par annonce)
- ✅ Notifications push (FCM)
- ✅ Signalement (annonce, message)
- ✅ Tableau de bord vendeur (vues, messages)
- ✅ Interface admin (stats, modération)
- ✅ Mention légale ("Tabdal ne remplace pas un notaire")
- ✅ Conformité CNDP (export/suppression données)

---

## Variables d'environnement requises

### 1. `app/google-services.json`

Remplacez le fichier placeholder par votre vrai `google-services.json` depuis la console Firebase :
- Créez un projet Firebase sur [console.firebase.google.com](https://console.firebase.google.com)
- Ajoutez une app Android avec le package `com.tabdal.android`
- Activez : **Authentication** (Email/Password + Phone), **Storage**, **Cloud Messaging**
- Téléchargez `google-services.json` et placez-le dans `app/`

### 2. `app/build.gradle.kts`

```kotlin
buildConfigField("String", "BASE_URL", "\"https://api.tabdal.ma/v1/\"")
buildConfigField("String", "MAPS_API_KEY", "\"YOUR_GOOGLE_MAPS_API_KEY\"")
```

Remplacez `YOUR_GOOGLE_MAPS_API_KEY` par votre clé Google Maps depuis [console.cloud.google.com](https://console.cloud.google.com).

### 3. `AndroidManifest.xml` – Maps API Key

La clé Maps est injectée automatiquement via `${MAPS_API_KEY}` depuis `BuildConfig`.

---

## Backend de test (mock)

En l'absence d'un backend réel, utilisez l'un de ces outils :

### Option A – MockAPI.io
Créez des endpoints REST qui retournent du JSON conforme aux DTOs :
- `GET /listings` → `ListingsPage`
- `POST /auth/login` → `AuthResponse`

### Option B – JSON Server (local)
```bash
npm install -g json-server
json-server --watch db.json --port 3000
```

Puis changez `BASE_URL` en `"http://10.0.2.2:3000/"` pour l'émulateur.

### Option C – Postman Mock Server
Créez un Mock Server dans Postman et pointez `BASE_URL` dessus.

---

## Builder l'APK

### Prérequis
- **Android Studio** Ladybug (2024.2.1) ou supérieur
- **JDK 17**
- **Gradle 8.9** (wrapper inclus)
- **minSdk** 26 (Android 8.0+)

### Étapes

1. **Cloner le projet**
```bash
git clone https://github.com/cortexai-2025/Tabdal.git
cd Tabdal
```

2. **Configurer Firebase**
   - Remplacez `app/google-services.json` par le vôtre

3. **Configurer les API keys** dans `app/build.gradle.kts`

4. **Builder le debug APK**
```bash
./gradlew assembleDebug
```
L'APK sera généré dans : `app/build/outputs/apk/debug/app-debug.apk`

5. **Builder le release APK**
```bash
./gradlew assembleRelease
```

6. **Installer sur émulateur**
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## Critères de validation MVP

| Fonctionnalité | Status |
|---|---|
| Inscription + SMS | ✅ Implémenté |
| Création annonce avec photos | ✅ Implémenté |
| Filtres + recherche | ✅ Implémenté |
| Messagerie | ✅ Implémenté |
| Signalement | ✅ Implémenté |
| Suppression compte + export | ✅ Implémenté |
| Back-office admin | ✅ Implémenté (vue basique) |

---

## Couleurs de la marque

| Nom | Hex |
|---|---|
| Primary (bleu pétrole) | `#1A4A6B` |
| Secondary (terracotta) | `#C0622D` |
| Tertiary (sable doré) | `#D4A96A` |

---

## Licence

© 2025 TABDAL – immobilier Maroc. Tous droits réservés.
