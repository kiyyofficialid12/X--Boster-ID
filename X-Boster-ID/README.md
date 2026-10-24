# X- Boster ID Elite — GitHub-ready base

Original Android Java gaming hub with an elite black-and-orange visual style. This repository is prepared for continued development with GitHub Copilot and a GitHub Actions debug-build workflow.

## Current base features
- Five-bar local X-Boost energy counter; one bar is consumed when launching a game from the app.
- Rewarded-ad energy and Telegram bot purchase entry points (require real service configuration).
- Animated intro, ambient orange HUD background, and game-launch transition.
- Game list from launchable Android activities.
- Local Gaming Mode toggle and quick links to Android settings.
- Bottom navigation for Home, Games, Boost, and Settings.

## Open in Android Studio
1. Extract this repository or clone it from GitHub.
2. Open the `X-Boster-ID-Elite-Base` folder in Android Studio.
3. Use JDK 17 and install Android SDK Platform 35.
4. Sync Gradle and select **Build → Build APK(s)**.

The repository currently does not include a Gradle Wrapper. On a machine with Gradle 8.7 installed, build with:

```bash
gradle --no-daemon assembleDebug
```

The GitHub Actions workflow sets up Gradle 8.7 and attempts to build the debug APK automatically on pushes and pull requests. Download the generated APK from **Actions → workflow run → Artifacts** if the workflow succeeds.

## Continue with GitHub Copilot
1. Create a new repository on GitHub, preferably private while developing.
2. Upload the contents of this folder to the repository root (not the ZIP file itself).
3. Open the repository in VS Code or GitHub and start Copilot Chat.
4. Ask Copilot for one change at a time, for example: `Read .github/copilot-instructions.md and inspect the current code. Add a proper Gradle Wrapper and keep the app buildable. Show the files changed and run the available build/tests.`
5. Review diffs and Actions results before merging changes.

## Important limitations
- Energy is a local feature-use counter, not a physical CPU/GPU energy supply or a performance boost.
- The rewarded-ad button is only an entry point until a compliant ad SDK is integrated. Grant energy only after a verified reward callback.
- Configure a real Telegram bot URL before using the purchase entry point. Never commit bot tokens, payment credentials, or signing keys.
- An ordinary Android app cannot reliably remove ads displayed inside third-party games or guarantee higher FPS / prevent crashes.
- Visual animations are not system-level CPU/GPU tuning. Test on a real Android device before release.

## App identity
`namespace` and `applicationId` are currently `com.space.gamebooster`. They are intentionally retained for compatibility; change them only alongside the Java package paths and any external service configuration.
