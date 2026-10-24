# GitHub Copilot instructions — X- Boster ID

## Project
- Native Android app written in Java using Android Views and Gradle Groovy DSL.
- App identity: X- Boster ID / X-BOOSTER ID; original black-and-orange gaming branding.
- Android namespace and applicationId currently remain `com.space.gamebooster` to avoid breaking the source package.
- Keep the app clearly independent; do not copy proprietary assets/code from ASUS ROG, TECNO, or GG Game Space.

## Build and quality
- Use JDK 17, Android SDK 35, Android Gradle Plugin 8.6.1, and Gradle 8.7.
- Build with `gradle assembleDebug` (or `./gradlew assembleDebug` once a Gradle Wrapper is added).
- Run `gradle test` if tests exist. Never claim a build passed unless the command actually succeeded.
- Keep changes small, explain changed files, and include manual Android test steps.

## Safety and Android constraints
- Do not claim that an energy counter increases CPU/GPU performance. It is a feature-use token only.
- Do not promise that the app blocks advertisements inside third-party games. DND can only reduce notifications.
- Rewarded-ad energy must only be granted after a verified SDK reward callback; never fake ad completion.
- Purchases must use a trusted payment provider or the user's configured Telegram bot URL. Never collect card/payment credentials in the app or commit secrets/tokens.
- Do not add root-only CPU/GPU overclocking or hidden behavior. Request only Android permissions necessary for the implemented feature.
- Any overlay feature must be opt-in, explain its permission, and provide an obvious close control.
- Persist energy locally with robust bounds and handle app lifecycle / game launch failures without losing energy unexpectedly.

## UI expectations
- Preserve the original X logo/identity, black background, orange glow, readable contrast, and accessible touch targets.
- Avoid claiming exact replication of any device manufacturer's proprietary Game Space.
- Update README and tests when behavior changes. Keep app `versionName` and documentation in sync.
