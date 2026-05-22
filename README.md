# Building Mobile Apps at Scale — 8-Week Mastery Plan

> Working through Gergely Orosz's *Building Mobile Apps at Scale: 39 Engineering Challenges* by building a real **Job Tracker** Android app (Track A) and writing study notes (Track B). Target: ~85% mastery (33 of 39 challenges) in 8 weeks at 1 hr/day, fitted to a FIFO roster.

---

## How this works

**Two tracks, matched to FIFO energy:**

| Track | What | When | Output |
|---|---|---|---|
| **A — Build** | Implement codeable challenges in the Job Tracker app | Days off (fresh brain, laptop) | Working features + commits |
| **B — Study** | Read chapter, write a note in your own words | On-site (tired, after shift, no dev needed) | Markdown notes in `/notes` |

**The rule of thumb:** if you can write code for it, it's Build. If it only exists at team/org scale, it's Study.

**Cadence:** ~6 build sessions + ~8 study sessions per 14-day swing. Misses after night shift are expected and built in — don't treat a skipped day as failure.

**The 6 skipped chapters** (read-only, no formal note — these are the most team-process-specific): 21 (Shared Architecture Across Apps), 22 (Tooling Maturity), 24 (Platform Libraries & Teams), 25 (Adopting New Languages), 27 (Cross-Platform Feature Dev), 28 (Cross-Platform vs Native). This is how we land at 85%, not a padded 100%.

---

## Track A — Build challenges (the app)

Each maps to a real feature of the Job Tracker. Sessions are 1 hr; bigger features span multiple sessions.

| # | Ch | Challenge | Job Tracker feature | Est. sessions |
|---|---|---|---|---|
| A1 | 16 | Modular Architecture & DI | Split into `:app`, `:data`, `:domain`, `:feature-*` modules + Hilt | 3 |
| A2 | 1 | State Management | Application list/detail state, survives rotation & process death | 2 |
| A3 | 14 | App State & Event-Driven Changes | Status changes (Applied→Interview→Offer) propagate cleanly | 2 |
| A4 | 7 | Offline Support | Add an application offline, sync when back online (Room + WorkManager) | 3 |
| A5 | 13 | Navigation Architecture | Type-safe nav between list, detail, add/edit | 2 |
| A6 | 4 | Deeplinks | Open a specific application via link/notification | 1 |
| A7 | 5 | Push & Background Notifications | Interview reminders | 2 |
| A8 | 6 | App Crashes | Wire up Crashlytics, force a test crash, read the report | 1 |
| A9 | 17 | Automated Testing | Unit tests for status logic + one UI test | 2 |
| A10 | 31 | Feature Flags | Toggle an experimental feature without a release | 1 |
| A11 | 32 | Performance | Measure cold start / a slow list, fix it, prove it with numbers | 2 |
| A12 | 33 | Analytics, Monitoring | Log key events (app added, status changed) | 1 |
| A13 | 39 | App Size | Measure APK, enable R8/shrinking, report before/after | 1 |

**Build total: ~23 sessions across 13 challenges.**

---

## Track B — Study challenges (notes)

Read the chapter, write 150–250 words in your own words answering: *what's the problem, why does it only appear at scale, what are the common approaches?* Aim for IELTS Band 7 clarity — this doubles as interview prep.

| # | Ch | Challenge | Note focus |
|---|---|---|---|
| B1 | 2 | Mistakes Are Hard to Revert | Why you can't "rollback" a shipped mobile build like a server |
| B2 | 3 | The Long Tail of Old App Versions | Supporting users who never update |
| B3 | 10 | Third-Party Libraries & SDKs | Hidden costs, vetting, the bloat/risk tradeoff |
| B4 | 11 | Device & OS Fragmentation | Testing matrix reality |
| B5 | 12 | In-App Purchases | Store rules, receipt validation, edge cases |
| B6 | 8 | Accessibility | Why it's an engineering concern, not an afterthought |
| B7 | 9 | CI/CD & The Build Train | The "train" model for releasing on a schedule |
| B8 | 15 | Localization | More than translation — dates, RTL, pluralization |
| B9 | 18 | Manual Testing | Where it still beats automation |
| B10 | 19 | Planning & Decision Making | How large mobile teams decide |
| B11 | 20 | Avoiding Stepping on Toes | Code ownership, module boundaries at scale |
| B12 | 23 | Scaling Build & Merge Times | Why a 200-engineer repo gets slow |
| B13 | 26 | Kotlin Multiplatform / KMM | The promise and the catch |
| B14 | 29 | Web, PWA & Backend-Driven | When the server drives the UI |
| B15 | 30 | Experimentation | A/B testing on mobile's hard constraints |
| B16 | 34 | Mobile On-Call | What you're even on-call *for* on mobile |
| B17 | 35 | Advanced Code Quality Checks | Static analysis, linters at scale (note: SonarSource sponsored this book) |
| B18 | 36 | Compliance, Privacy & Security | GDPR, data handling — ties to your cybersecurity interest |
| B19 | 37 | Client-Side Data Migrations | Migrating local DB schemas on devices you don't control |
| B20 | 38 | Forced Upgrading | When and how to force users onto a new version |

**Study total: 20 notes.**

---

## The 8-week timeline

Weeks are framed around a generic swing. Slide them to your real roster dates. "On-site" weeks lean Study; "days off" lean Build.

| Week | Phase | Focus | Milestone |
|---|---|---|---|
| **1** | Foundation | A1 (modules + Hilt) on days off · B1, B2, B3 on-site | Repo created, app skeleton compiles, 3 notes |
| **2** | Core state | A2, A3 · B4, B5, B6 | App holds & displays job list reliably |
| **3** | Data layer | A4 (offline) start · B7, B8, B9 | Room DB working, can add offline |
| **4** | Data + nav | A4 finish, A5 · B10, B11 | Offline sync proven, navigation type-safe |
| **5** | Connect | A6, A7, A8 · B12, B13, B14 | Deeplinks + reminders + crash reporting live |
| **6** | Quality | A9, A10 · B15, B16, B17 | Tests passing, one feature behind a flag |
| **7** | Polish | A11, A12, A13 · B18, B19, B20 | Perf measured & improved, analytics in, APK shrunk |
| **8** | Ship & reflect | Buffer for spillover · README + closing thoughts | App on GitHub, all notes done, ~85% reached |

> Week 8 is deliberately light — it's your catch-up buffer for the night-shift days you'll inevitably miss. Don't fill it; protect it.

---

## A-to-Z Kanban board

Move cards left→right. Keep "In Progress" to **one Build + one Study** at a time — focus beats juggling, especially on tired days.

### 📋 Backlog
*(everything starts here — full list above: A1–A13, B1–B20)*

### 🎯 This Swing
*(pull ~6 Build + ~8 Study cards here at the start of each 14-day cycle)*

### 🔨 In Progress
*(max 1 Build + 1 Study)*

### 👀 Review
*(Build: does it run + is it committed? · Study: re-read your note — could you say it out loud in an interview?)*

### ✅ Done
*(Build: merged to main · Study: note in `/notes`, committed)*

### ⏭️ Skipped (read-only)
*Ch 21, 22, 24, 25, 27, 28 — read, no formal note.*

---

## Repo structure (suggested)

```
job-tracker/
├── README.md              ← project + this plan + progress badges
├── notes/                 ← Track B lives here
│   ├── 02-hard-to-revert.md
│   ├── 03-old-versions.md
│   └── ...
├── app/                   ← Track A
├── data/
├── domain/
└── feature-applications/
```

Each note committed separately = a green contribution graph that *tells a story* to anyone viewing your profile: "this person worked through a serious industry book methodically." That's portfolio gold for your job hunt.

---

## Definition of "mastered" (your 85%)

- **Build (13):** feature runs, is committed, and you can explain the design choice.
- **Study (20):** note written in your own words, and you could answer a "how would you handle X at scale?" interview question on it.
- **33 of 39 = 85%.** The 6 skipped are read-but-not-noted. Honest target, honestly hit.

---

## First three sessions — start here

1. **Day off:** Create the GitHub repo, set up the multi-module skeleton + Hilt (A1, session 1 of 3).
2. **On-site:** Read Ch 2, write `02-hard-to-revert.md` (B1).
3. **On-site:** Read Ch 3, write `03-old-versions.md` (B2).

That's it. One hour, one card, one commit at a time.
