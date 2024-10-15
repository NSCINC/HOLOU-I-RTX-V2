import 'package:flutter/material.dart';

void main() {
  runApp(HoloRTxFlexApp());
}

class HoloRTxFlexApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'HoloRTx Flex',
      home: HoloRTxFlexHome(),
    );
  }
}

class HoloRTxFlexHome extends StatefulWidget {
  @override
  _HoloRTxFlexHomeState createState() => _HoloRTxFlexHomeState();
}

class _HoloRTxFlexHomeState extends State<HoloRTxFlexHome> {
  final TextEditingController _controller = TextEditingController();
  String _displayText = '';

  void _saveInput() {
    setState(() {
      _displayText = _controller.text; // Salva o texto digitado
      _controller.clear(); // Limpa o campo de entrada após salvar
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('HoloRTx Flex'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: <Widget>[
            TextField(
              controller: _controller,
              decoration: InputDecoration(
                hintText: 'Digite algo',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 16), // Espaçamento entre o TextField e o botão
            ElevatedButton(
              onPressed: _saveInput,
              child: Text('Salvar'),
            ),
            SizedBox(height: 16), // Espaçamento entre o botão e o TextView
            Text(
              _displayText,
              style: TextStyle(fontSize: 18),
            ),
          ],
        ),
      ),
    );
  }
}
