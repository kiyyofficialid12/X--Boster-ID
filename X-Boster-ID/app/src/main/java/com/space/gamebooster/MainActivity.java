package com.space.gamebooster;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/** X- Boster ID Elite: original animated gaming hub UI. Does not change system CPU/GPU clocks. */
public class MainActivity extends Activity {
    private final int bg = Color.rgb(7, 7, 9);
    private final int panel = Color.rgb(25, 20, 18);
    private final int orange = Color.rgb(255, 126, 28);
    private final int orangeLight = Color.rgb(255, 183, 83);
    private final int muted = Color.rgb(177, 164, 153);
    private LinearLayout root;
    private boolean gamingMode = false;
    private TextView modeStatus;
    private Button modeButton;
    private String currentTab = "Beranda";
    private static final int MAX_ENERGY = 5;
    private int energy = MAX_ENERGY;
    private TextView energyCount;
    private LinearLayout energyBars;
    // Set this to your real Telegram bot URL before publishing, e.g. https://t.me/YourBot?start=energy
    private static final String ENERGY_BOT_URL = "";
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(10, 8, 7));
        getWindow().setNavigationBarColor(Color.rgb(10, 8, 7));
        getWindow().getDecorView().setSystemUiVisibility(0);
        energy = getSharedPreferences("xbooster", MODE_PRIVATE).getInt("energy", MAX_ENERGY);
        showIntro();
    }

    private void showIntro() {
        FrameLayout frame = new FrameLayout(this);
        frame.setBackgroundColor(bg);
        frame.addView(new EliteBackground(this), new FrameLayout.LayoutParams(-1, -1));
        LinearLayout content = new LinearLayout(this);
        content.setGravity(Gravity.CENTER);
        content.setOrientation(LinearLayout.VERTICAL);
        TextView emblem = text("X", 78, orange, true);
        emblem.setGravity(Gravity.CENTER);
        GradientDrawable ring = new GradientDrawable(); ring.setShape(GradientDrawable.OVAL);
        ring.setColor(Color.rgb(24, 15, 10)); ring.setStroke(dp(3), orange);
        emblem.setBackground(ring); emblem.setElevation(dp(12));
        content.addView(emblem, new LinearLayout.LayoutParams(dp(142), dp(142)));
        TextView title = text("X- BOSTER ID", 27, Color.WHITE, true);
        title.setGravity(Gravity.CENTER); title.setLetterSpacing(.08f);
        content.addView(title, margins(0, 22, 0, 2));
        TextView sub = text("ELITE GAMING SYSTEM", 11, orangeLight, true);
        sub.setGravity(Gravity.CENTER); sub.setLetterSpacing(.22f);
        content.addView(sub, margins(0, 0, 0, 25));
        ProgressBar progress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progress.setIndeterminate(true);
        content.addView(progress, new LinearLayout.LayoutParams(dp(190), dp(3)));
        TextView loading = text("INITIALIZING BOOST ENGINE", 9, muted, true);
        loading.setGravity(Gravity.CENTER); content.addView(loading, margins(0, 12, 0, 0));
        frame.addView(content, new FrameLayout.LayoutParams(-1, -1));
        setContentView(frame);
        emblem.setScaleX(.55f); emblem.setScaleY(.55f); emblem.setAlpha(.15f);
        emblem.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(850).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        title.setAlpha(0f); title.animate().alpha(1f).setStartDelay(350).setDuration(650).start();
        handler.postDelayed(this::showHome, 1900);
    }

    private void setup(String tab) {
        currentTab = tab;
        FrameLayout frame = new FrameLayout(this);
        frame.setBackgroundColor(bg);
        frame.addView(new EliteBackground(this), new FrameLayout.LayoutParams(-1, -1));
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true); scroll.setBackgroundColor(Color.TRANSPARENT);
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(17), dp(17), dp(17), dp(14));
        scroll.addView(root);
        frame.addView(scroll, new FrameLayout.LayoutParams(-1, -1));
        setContentView(frame);
    }

    private void showHome() {
        setup("Beranda"); addBrand();
        LinearLayout hero = card(); hero.setPadding(dp(18), dp(20), dp(18), dp(18));
        root.addView(hero, margins(0, 15, 0, 15));
        hero.addView(text("ELITE PERFORMANCE CENTER", 10, orangeLight, true));
        hero.addView(text("Gaming Mode", 29, Color.WHITE, true), margins(0, 5, 0, 3));
        hero.addView(text("Satu panel. Fokus penuh. Siap bertanding.", 12, muted, false), margins(0, 0, 0, 18));
        LinearLayout statusRow = new LinearLayout(this); statusRow.setGravity(Gravity.CENTER_VERTICAL);
        TextView orb = text(gamingMode ? "◉" : "◎", 31, orange, true); statusRow.addView(orb, new LinearLayout.LayoutParams(dp(48), dp(48)));
        LinearLayout words = new LinearLayout(this); words.setOrientation(LinearLayout.VERTICAL);
        words.addView(text("STATUS GAMING PROFILE", 9, muted, true));
        modeStatus = text(gamingMode ? "ELITE MODE AKTIF" : "SIAP DIAKTIFKAN", 14, gamingMode ? orangeLight : Color.WHITE, true);
        words.addView(modeStatus, margins(0, 3, 0, 0)); statusRow.addView(words, new LinearLayout.LayoutParams(0, -2, 1)); hero.addView(statusRow, margins(0, 0, 0, 15));
        modeButton = button(gamingMode ? "NONAKTIFKAN ELITE MODE" : "AKTIFKAN ELITE MODE");
        hero.addView(modeButton, new LinearLayout.LayoutParams(-1, dp(49)));
        modeButton.setOnClickListener(v -> { gamingMode = !gamingMode; modeStatus.setText(gamingMode ? "ELITE MODE AKTIF" : "SIAP DIAKTIFKAN"); modeStatus.setTextColor(gamingMode ? orangeLight : Color.WHITE); modeButton.setText(gamingMode ? "NONAKTIFKAN ELITE MODE" : "AKTIFKAN ELITE MODE"); Toast.makeText(this, gamingMode ? "Profil Elite diaktifkan" : "Profil Elite dinonaktifkan", Toast.LENGTH_SHORT).show(); });
        root.addView(energyCard(), margins(0, 0, 0, 15));
        root.addView(sectionTitle("Akses cepat", "TOOLS"), margins(2, 1, 0, 10));
        LinearLayout grid = new LinearLayout(this); root.addView(grid, margins(0, 0, 0, 15));
        grid.addView(quickTile("◈", "Daftar Game", "Pilih game", v -> showGames()), weighted());
        grid.addView(quickTile("◌", "Jangan Ganggu", "Notifikasi", v -> openSettings(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)), weighted());
        grid.addView(quickTile("ϟ", "Baterai", "Mode daya", v -> openSettings(Settings.ACTION_BATTERY_SAVER_SETTINGS)), weighted());
        root.addView(sectionTitle("Game & aplikasi", "QUICK LAUNCH"), margins(2, 0, 0, 10));
        List<ResolveInfo> games = getLaunchableApps();
        if (games.isEmpty()) root.addView(infoCard("Belum ada aplikasi", "Aplikasi yang bisa dibuka akan tampil di tab Game."));
        else for (int i = 0; i < Math.min(3, games.size()); i++) root.addView(appRow(games.get(i)), margins(0, 0, 0, 8));
        root.addView(infoCard("Elite system", "Animasi booster adalah efek visual. Android biasa tidak memberi aplikasi akses bebas untuk menaikkan clock CPU/GPU atau menjamin FPS."), margins(0, 12, 0, 12));
        addBottomNav();
    }

    private void showGames() {
        setup("Game"); addBrand();
        root.addView(text("Game Library", 26, Color.WHITE, true), margins(0, 18, 0, 4));
        root.addView(text("Pilih game untuk memulai animasi X-Boost.", 12, muted, false), margins(0, 0, 0, 14));
        List<ResolveInfo> apps = getLaunchableApps();
        if (apps.isEmpty()) root.addView(infoCard("Aplikasi tidak ditemukan", "Pastikan ada aplikasi yang bisa diluncurkan."));
        for (ResolveInfo app : apps) root.addView(appRow(app), margins(0, 0, 0, 9));
        root.addView(text("X-Boost menampilkan transisi visual sebelum aplikasi dibuka; fitur ini tidak mengubah sistem internal game.", 10, muted, false), margins(2, 12, 2, 18));
        addBottomNav();
    }

    private void showBoost() {
        setup("Boost"); addBrand();
        root.addView(text("Elite Game Tools", 25, Color.WHITE, true), margins(0, 18, 0, 4));
        root.addView(text("Kontrol praktis sebelum sesi bermain.", 12, muted, false), margins(0, 0, 0, 15));
        root.addView(actionCard("◉", "Jangan Ganggu", "Buka akses notifikasi Android", v -> openSettings(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)), margins(0, 0, 0, 9));
        root.addView(actionCard("ϟ", "Penghemat Baterai", "Buka pengaturan daya", v -> openSettings(Settings.ACTION_BATTERY_SAVER_SETTINGS)), margins(0, 0, 0, 9));
        root.addView(actionCard("⌘", "Pengaturan Sistem", "Atur layar dan refresh rate jika tersedia", v -> openSettings(Settings.ACTION_SETTINGS)), margins(0, 0, 0, 9));
        root.addView(energyCard(), margins(0, 0, 0, 12));
        root.addView(infoCard("X-BOOST VISUAL", "Energi mengaktifkan sesi animasi booster. Ini bukan overclock CPU/GPU dan tidak menjamin kenaikan FPS."), margins(0, 10, 0, 16));
        addBottomNav();
    }

    private void showSettings() {
        setup("Pengaturan"); addBrand();
        root.addView(text("Pengaturan X- Boster ID", 24, Color.WHITE, true), margins(0, 18, 0, 14));
        root.addView(actionCard("◐", "Tema Elite", "Hitam metalik dengan aksen oranye", v -> Toast.makeText(this, "Tema Elite aktif", Toast.LENGTH_SHORT).show()), margins(0, 0, 0, 9));
        root.addView(actionCard("ⓘ", "Tentang aplikasi", "X- Boster ID · Elite v1.3.0", v -> Toast.makeText(this, "X- Boster ID Elite v1.3.0", Toast.LENGTH_SHORT).show()), margins(0, 0, 0, 9));
        root.addView(infoCard("Info", "Launcher game, animasi intro, X-Boost launch transition, dan pintasan pengaturan Android."), margins(0, 10, 0, 16));
        addBottomNav();
    }

    private void addBrand() {
        LinearLayout row = new LinearLayout(this); row.setGravity(Gravity.CENTER_VERTICAL);
        TextView logo = text("X", 27, orange, true); logo.setGravity(Gravity.CENTER);
        GradientDrawable badge = new GradientDrawable(); badge.setShape(GradientDrawable.OVAL); badge.setColor(Color.rgb(30, 17, 9)); badge.setStroke(dp(2), orange); logo.setBackground(badge);
        row.addView(logo, new LinearLayout.LayoutParams(dp(44), dp(44)));
        LinearLayout words = new LinearLayout(this); words.setOrientation(LinearLayout.VERTICAL); words.setPadding(dp(10), 0, 0, 0);
        words.addView(text("X- BOSTER ID", 21, Color.WHITE, true)); words.addView(text("ELITE GAMING HUB", 9, orangeLight, true));
        row.addView(words, new LinearLayout.LayoutParams(0, -2, 1)); row.addView(text("v1.3", 10, muted, true)); root.addView(row);
    }

    private List<ResolveInfo> getLaunchableApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null); intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> result = getPackageManager().queryIntentActivities(intent, 0);
        Collections.sort(result, new Comparator<ResolveInfo>() { @Override public int compare(ResolveInfo a, ResolveInfo b) { return String.valueOf(a.loadLabel(getPackageManager())).compareToIgnoreCase(String.valueOf(b.loadLabel(getPackageManager()))); }});
        List<ResolveInfo> filtered = new ArrayList<>();
        for (ResolveInfo info : result) if (info.activityInfo != null && !getPackageName().equals(info.activityInfo.packageName)) filtered.add(info);
        return filtered;
    }

    private View appRow(ResolveInfo info) {
        LinearLayout row = card(); row.setOrientation(LinearLayout.HORIZONTAL); row.setGravity(Gravity.CENTER_VERTICAL); row.setPadding(dp(12), dp(10), dp(12), dp(10));
        TextView icon = text("⬡", 27, orange, true); icon.setGravity(Gravity.CENTER); row.addView(icon, new LinearLayout.LayoutParams(dp(48), dp(48)));
        LinearLayout labels = new LinearLayout(this); labels.setOrientation(LinearLayout.VERTICAL);
        labels.addView(text(String.valueOf(info.loadLabel(getPackageManager())), 14, Color.WHITE, true));
        labels.addView(text(info.activityInfo.packageName, 9, muted, false), margins(0, 3, 0, 0));
        row.addView(labels, new LinearLayout.LayoutParams(0, -2, 1)); row.addView(text("X-BOOST ›", 10, orangeLight, true));
        row.setOnClickListener(v -> { if (energy <= 0) showEnergyEmpty(); else showLaunchBoost(info); }); return row;
    }

    private void showLaunchBoost(ResolveInfo info) {
        Dialog dialog = new Dialog(this); dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        FrameLayout frame = new FrameLayout(this); frame.setBackgroundColor(Color.rgb(8, 7, 7)); frame.addView(new EliteBackground(this), new FrameLayout.LayoutParams(-1, -1));
        LinearLayout content = new LinearLayout(this); content.setGravity(Gravity.CENTER); content.setOrientation(LinearLayout.VERTICAL); content.setPadding(dp(24), dp(24), dp(24), dp(24));
        TextView ring = text("X", 68, orange, true); ring.setGravity(Gravity.CENTER);
        GradientDrawable ringBg = new GradientDrawable(); ringBg.setShape(GradientDrawable.OVAL); ringBg.setColor(Color.rgb(25, 15, 9)); ringBg.setStroke(dp(3), orange); ring.setBackground(ringBg); ring.setElevation(dp(12));
        content.addView(ring, new LinearLayout.LayoutParams(dp(126), dp(126)));
        TextView title = text("X-BOOST SEQUENCE", 20, Color.WHITE, true); title.setGravity(Gravity.CENTER); title.setLetterSpacing(.08f); content.addView(title, margins(0, 24, 0, 6));
        TextView game = text(String.valueOf(info.loadLabel(getPackageManager())).toUpperCase(), 12, orangeLight, true); game.setGravity(Gravity.CENTER); content.addView(game, margins(0, 0, 0, 22));
        ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal); bar.setIndeterminate(true); content.addView(bar, new LinearLayout.LayoutParams(dp(220), dp(4)));
        TextView status = text("CALIBRATING VISUAL PROFILE  •  READY", 9, muted, true); status.setGravity(Gravity.CENTER); content.addView(status, margins(0, 13, 0, 0));
        frame.addView(content, new FrameLayout.LayoutParams(-1, -1)); dialog.setContentView(frame); dialog.setCancelable(false);
        Window w = dialog.getWindow(); if (w != null) { w.setBackgroundDrawableResource(android.R.color.transparent); w.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT); w.setDimAmount(0f); }
        ring.setScaleX(.4f); ring.setScaleY(.4f); ring.setAlpha(.2f); ring.animate().scaleX(1f).scaleY(1f).alpha(1f).rotationBy(360f).setDuration(1200).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        dialog.show(); if (dialog.getWindow() != null) dialog.getWindow().setLayout(-1, -1);
        handler.postDelayed(() -> {
            try { dialog.dismiss(); Intent launch = getPackageManager().getLaunchIntentForPackage(info.activityInfo.packageName); if (launch != null) { launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(launch); consumeEnergy(); } else Toast.makeText(this, "Aplikasi tidak bisa dibuka langsung", Toast.LENGTH_SHORT).show(); }
            catch (Exception e) { try { dialog.dismiss(); } catch (Exception ignored) {} Toast.makeText(this, "Tidak dapat membuka aplikasi ini", Toast.LENGTH_SHORT).show(); }
        }, 1750);
    }

    private View energyCard() {
        LinearLayout box = card(); box.setPadding(dp(14), dp(14), dp(14), dp(14));
        LinearLayout top = new LinearLayout(this); top.setGravity(Gravity.CENTER_VERTICAL);
        TextView bolt = text("ϟ", 30, orange, true); top.addView(bolt, new LinearLayout.LayoutParams(dp(42), dp(42)));
        LinearLayout titles = new LinearLayout(this); titles.setOrientation(LinearLayout.VERTICAL);
        titles.addView(text("ENERGI X-BOOST", 14, Color.WHITE, true));
        energyCount = text(energy + " / " + MAX_ENERGY + " batang", 11, orangeLight, true); titles.addView(energyCount, margins(0, 3, 0, 0));
        top.addView(titles, new LinearLayout.LayoutParams(0, -2, 1));
        top.addView(text("ELITE", 10, orange, true)); box.addView(top);
        energyBars = new LinearLayout(this); energyBars.setOrientation(LinearLayout.HORIZONTAL); energyBars.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams bp = margins(0, 10, 0, 12); box.addView(energyBars, bp); refreshEnergyBars();
        box.addView(text("1 batang digunakan setiap kali kamu meluncurkan game lewat X-Boost.", 10, muted, false), margins(0, 0, 0, 10));
        LinearLayout actions = new LinearLayout(this); actions.setOrientation(LinearLayout.HORIZONTAL);
        Button free = button("Energi gratis (iklan)"); actions.addView(free, weighted());
        Button buy = button("Beli via bot"); actions.addView(buy, weighted());
        free.setOnClickListener(v -> showRewardAdInfo()); buy.setOnClickListener(v -> openEnergyBot());
        box.addView(actions); return box;
    }

    private void refreshEnergyBars() {
        if (energyBars == null || energyCount == null) return;
        energyBars.removeAllViews();
        for (int i = 0; i < MAX_ENERGY; i++) {
            View bar = new View(this); GradientDrawable shape = new GradientDrawable(); shape.setCornerRadius(dp(5));
            shape.setColor(i < energy ? orange : Color.rgb(55, 47, 42));
            shape.setStroke(dp(1), i < energy ? orangeLight : Color.rgb(90, 72, 60)); bar.setBackground(shape);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(9), 1); lp.setMargins(0, 0, dp(5), 0); energyBars.addView(bar, lp);
        }
        energyCount.setText(energy + " / " + MAX_ENERGY + " batang");
    }

    private void consumeEnergy() {
        energy = Math.max(0, energy - 1);
        getSharedPreferences("xbooster", MODE_PRIVATE).edit().putInt("energy", energy).apply();
        if (energyBars != null) refreshEnergyBars();
        Toast.makeText(this, "Energi X-Boost tersisa " + energy + "/" + MAX_ENERGY, Toast.LENGTH_SHORT).show();
    }

    private void showEnergyEmpty() {
        new AlertDialog.Builder(this).setTitle("Energi X-Boost habis")
            .setMessage("Energi digunakan untuk menjalankan sesi X-Boost. Dapatkan energi melalui iklan berhadiah setelah SDK iklan dikonfigurasi, atau beli melalui bot resmi.")
            .setPositiveButton("Iklan berhadiah", (d, w) -> showRewardAdInfo())
            .setNeutralButton("Beli via bot", (d, w) -> openEnergyBot())
            .setNegativeButton("Nanti", null).show();
    }

    private void showRewardAdInfo() {
        new AlertDialog.Builder(this).setTitle("Energi gratis melalui iklan")
            .setMessage("Fitur ini memerlukan integrasi iklan berhadiah (misalnya Google AdMob Rewarded Ads), Ad Unit ID, dan verifikasi callback reward. Belum ada iklan sungguhan yang diputar dan energi tidak akan ditambahkan sampai integrasi tersebut dipasang.")
            .setPositiveButton("Mengerti", null).show();
    }

    private void openEnergyBot() {
        if (ENERGY_BOT_URL.trim().isEmpty()) {
            new AlertDialog.Builder(this).setTitle("Hubungkan bot penjualan")
                .setMessage("Tambahkan tautan bot Telegram milikmu pada konstanta ENERGY_BOT_URL di MainActivity.java. Pembelian energi belum aktif dan aplikasi ini tidak memproses pembayaran.")
                .setPositiveButton("OK", null).show();
            return;
        }
        try { startActivity(new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(ENERGY_BOT_URL))); }
        catch (Exception e) { Toast.makeText(this, "Tidak dapat membuka tautan bot", Toast.LENGTH_SHORT).show(); }
    }

    private View quickTile(String symbol, String title, String desc, View.OnClickListener listener) {
        LinearLayout tile = card(); tile.setPadding(dp(7), dp(13), dp(7), dp(13)); tile.setGravity(Gravity.CENTER);
        tile.addView(text(symbol, 25, orange, true)); TextView name = text(title, 11, Color.WHITE, true); name.setGravity(Gravity.CENTER); tile.addView(name, margins(0, 7, 0, 3));
        TextView small = text(desc, 9, muted, false); small.setGravity(Gravity.CENTER); tile.addView(small); tile.setOnClickListener(listener); return tile;
    }

    private View actionCard(String icon, String title, String desc, View.OnClickListener listener) {
        LinearLayout row = card(); row.setOrientation(LinearLayout.HORIZONTAL); row.setGravity(Gravity.CENTER_VERTICAL); row.setPadding(dp(13), dp(13), dp(13), dp(13));
        row.addView(text(icon, 24, orange, true), new LinearLayout.LayoutParams(dp(42), dp(42)));
        LinearLayout words = new LinearLayout(this); words.setOrientation(LinearLayout.VERTICAL); words.addView(text(title, 14, Color.WHITE, true)); words.addView(text(desc, 11, muted, false), margins(0, 4, 0, 0));
        row.addView(words, new LinearLayout.LayoutParams(0, -2, 1)); row.addView(text("›", 26, orange, true)); row.setOnClickListener(listener); return row;
    }

    private View infoCard(String title, String body) { LinearLayout box = card(); box.setPadding(dp(14), dp(13), dp(14), dp(13)); box.addView(text(title, 13, orangeLight, true)); box.addView(text(body, 11, muted, false), margins(0, 6, 0, 0)); return box; }
    private TextView sectionTitle(String title, String sub) { TextView t = text(title, 16, Color.WHITE, true); return t; }

    private void addBottomNav() {
        LinearLayout nav = card(); nav.setOrientation(LinearLayout.HORIZONTAL); nav.setPadding(dp(4), dp(8), dp(4), dp(8));
        String[] names = {"Beranda", "Game", "Boost", "Pengaturan"}; String[] icons = {"⌂", "⬡", "ϟ", "⚙"};
        for (int i = 0; i < names.length; i++) { final String name = names[i]; LinearLayout item = new LinearLayout(this); item.setOrientation(LinearLayout.VERTICAL); item.setGravity(Gravity.CENTER);
            item.addView(text(icons[i], 19, currentTab.equals(name) ? orange : muted, true)); TextView label = text(name, 9, currentTab.equals(name) ? orangeLight : muted, true); label.setGravity(Gravity.CENTER); item.addView(label, margins(0, 3, 0, 0));
            item.setOnClickListener(v -> { if ("Beranda".equals(name)) showHome(); else if ("Game".equals(name)) showGames(); else if ("Boost".equals(name)) showBoost(); else showSettings(); }); nav.addView(item, new LinearLayout.LayoutParams(0, -2, 1)); }
        root.addView(nav, margins(0, 16, 0, 4));
    }

    private void openSettings(String action) { try { startActivity(new Intent(action)); } catch (Exception e) { try { startActivity(new Intent(Settings.ACTION_SETTINGS)); } catch (Exception ignored) { Toast.makeText(this, "Pengaturan tidak tersedia", Toast.LENGTH_SHORT).show(); } } }
    private LinearLayout card() { LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); GradientDrawable shape = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{panel, Color.rgb(12, 11, 11)}); shape.setCornerRadius(dp(17)); shape.setStroke(dp(1), Color.rgb(119, 62, 28)); box.setBackground(shape); return box; }
    private TextView text(String value, int size, int color, boolean bold) { TextView t = new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(color); if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD); return t; }
    private Button button(String label) { Button b = new Button(this); b.setText(label); b.setTextSize(12); b.setTextColor(Color.WHITE); b.setAllCaps(false); b.setTypeface(Typeface.DEFAULT, Typeface.BOLD); GradientDrawable shape = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.rgb(189, 70, 12), Color.rgb(255, 145, 35)}); shape.setCornerRadius(dp(14)); b.setBackground(shape); return b; }
    private LinearLayout.LayoutParams margins(int l, int t, int r, int b) { LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2); p.setMargins(dp(l), dp(t), dp(r), dp(b)); return p; }
    private LinearLayout.LayoutParams weighted() { LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, -2, 1); p.setMargins(dp(3), 0, dp(3), 0); return p; }
    private int dp(float value) { return (int)(value * getResources().getDisplayMetrics().density + 0.5f); }

    /** Animated, lightweight ambient background: moving sparks, orbital rings and orange glow. */
    private class EliteBackground extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Random random = new Random(84);
        private final float[] xs = new float[34], ys = new float[34], speeds = new float[34];
        private long started = System.currentTimeMillis();
        EliteBackground(Activity context) { super(context); for (int i=0;i<xs.length;i++) { xs[i]=random.nextFloat(); ys[i]=random.nextFloat(); speeds[i]=.15f+random.nextFloat()*.7f; } setLayerType(View.LAYER_TYPE_SOFTWARE, null); }
        @Override protected void onDraw(Canvas c) {
            super.onDraw(c); float w=getWidth(), h=getHeight(); if (w<=0||h<=0) return;
            c.drawColor(bg); float t=(System.currentTimeMillis()-started)/1000f;
            paint.setShader(new android.graphics.RadialGradient(w*.05f,h*.2f,Math.max(w,h)*.75f,new int[]{Color.rgb(48,23,9),Color.rgb(17,12,10),bg},null,android.graphics.Shader.TileMode.CLAMP)); c.drawRect(0,0,w,h,paint); paint.setShader(null);
            paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(dp(1)); paint.setColor(Color.argb(45,255,135,45));
            float radius=Math.min(w,h)*(.26f+(float)Math.sin(t*.8f)*.012f); c.drawCircle(w*.92f,h*.19f,radius,paint); c.drawCircle(w*.92f,h*.19f,radius+dp(12),paint); c.drawCircle(w*.92f,h*.19f,radius+dp(25),paint);
            paint.setStyle(Paint.Style.FILL);
            for(int i=0;i<xs.length;i++){ float y=((ys[i]*h)+(t*dp(12)*speeds[i]))%(h+dp(20)); float x=xs[i]*w+(float)Math.sin(t*.6f+i)*dp(8); int alpha=65+(int)(90*(.5+.5*Math.sin(t*2+i))); paint.setColor(Color.argb(alpha,255,145,64)); c.drawCircle(x,y,dp(i%5==0?2:1),paint); }
            paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(dp(2)); paint.setColor(Color.argb(80,255,126,28)); c.drawArc(w*.55f-dp(35),h*.62f-dp(35),w*.55f+dp(35),h*.62f+dp(35),(t*35)%360,95,false); paint.setStyle(Paint.Style.FILL);
            postInvalidateDelayed(40);
        }
    }
}
