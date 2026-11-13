# Getting the APK built with GitHub (বাংলা + English)

You need a **computer** with a browser for the upload step (GitHub's website cannot upload folders from a phone). The build itself runs on GitHub's servers; you only download the finished APK.

## ধাপ ১ — প্রজেক্ট আপলোড (একবারই)
1. `TutorFlow.zip` কম্পিউটারে **Extract** করুন।
2. github.com-এ লগইন করে **New repository** খুলুন। নাম `TutorFlow`, **Private** রাখুন। Create করুন।
3. খালি repository-র পাতায় **uploading an existing file** লিংকে চাপুন।
4. Extract করা `TutorFlow` ফোল্ডারের **ভেতরের সব ফাইল ও ফোল্ডার** ব্রাউজারে টেনে ছেড়ে দিন (ফোল্ডারটা নিজে নয়, তার ভেতরেরগুলো)।
   - **`.github` ফোল্ডারটা অবশ্যই থাকতে হবে।** Mac-এ লুকানো থাকে: Finder-এ `Cmd + Shift + .` চাপুন। Windows-এ সাধারণত দেখা যায়।
   - `.github` ফোল্ডার না গেলে: **Add file → Create new file**, নামের ঘরে `.github/workflows/android.yml` লিখে, ZIP-এর ভেতরের একই নামের ফাইলের লেখা কপি-পেস্ট করুন।
5. নিচে **Commit changes** চাপুন।

## ধাপ ২ — বিল্ড দেখা ও APK নামানো
1. Repository-র **Actions** ট্যাব খুলুন। "Android build" নামে একটা কাজ চলতে দেখবেন (৫–১০ মিনিট লাগে)। না চললে বাম পাশে "Android build" → **Run workflow**।
2. সবুজ টিক ✅ এলে কাজটায় ঢুকুন। একদম নিচে **Artifacts** অংশে `TutorFlow-debug-apk` পাবেন। ডাউনলোড করুন, ZIP খুলুন, ভেতরে `app-debug.apk`।
3. APK ফোনে পাঠান, খুলুন। "Install unknown apps" অনুমতি চাইলে এই একবারের জন্য দিন।

## বিল্ড লাল ❌ হলে
কাজটায় ঢুকে লাল ধাপের লেখা (error লাইনগুলো) কপি করে আমাকে পাঠান। আমি ঠিক করে নতুন ফাইল দেব। প্রথমবারে এরর আসা স্বাভাবিক, কারণ আমার এখানে কম্পাইল করার উপায় নেই।

## Notes
- This is a **debug** APK (for testing). The signed **release** APK is built at the end (Phase 8) with your own keystore.
- Debug APKs are signed with a temporary debug key. Install a later release build only after uninstalling the debug one.
