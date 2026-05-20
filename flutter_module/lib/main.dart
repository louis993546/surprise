import 'package:flutter/material.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Page',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF00B0FF),
          brightness: Brightness.light,
        ),
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatelessWidget {
  const MyHomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      backgroundColor: Color(0xFFF5F5DC), // Matches the book cream color
      body: SafeArea(
        child: Padding(
          padding: EdgeInsets.all(48.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                'The Flutter Page',
                style: TextStyle(
                  fontSize: 42,
                  fontWeight: FontWeight.bold,
                  color: Color(0xFF3E2723), // Deep warm brown
                ),
                textAlign: TextAlign.center,
              ),
              SizedBox(height: 16),
              // Pill badge
              WidgetBadge(),
              SizedBox(height: 32),
              // Explanation card
              ExplanationCard(),
            ],
          ),
        ),
      ),
    );
  }
}

class WidgetBadge extends StatelessWidget {
  const WidgetBadge({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
      decoration: BoxDecoration(
        color: const Color(0xFFE0F2FE), // Very light blue
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: const Color(0xFFBAE6FD)),
      ),
      child: const Text(
        'Rendered in Flutter',
        style: TextStyle(
          fontSize: 14,
          fontWeight: FontWeight.bold,
          color: Color(0xFF0369A1), // Deep blue
        ),
      ),
    );
  }
}

class ExplanationCard extends StatelessWidget {
  const ExplanationCard({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(32),
      decoration: BoxDecoration(
        color: const Color(0xFFEFEBE9), // Warm grey-cream card
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.05),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: const Text(
        'This side is rendered using Flutter. It runs inside an embedded FlutterView, powered by the Dart VM. It demonstrates hybrid cross-platform technology executing inside a native Jetpack Compose book, enabling seamless UI embedding.',
        style: TextStyle(
          fontSize: 18,
          height: 1.6,
          color: Color(0xFF5D4037),
        ),
        textAlign: TextAlign.center,
      ),
    );
  }
}
