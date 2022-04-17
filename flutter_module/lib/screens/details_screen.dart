import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';

import '../models/billionaire.dart';
import '../utils.dart';

class DetailsScreen extends StatefulWidget {
  const DetailsScreen({Key? key}) : super(key: key);
  static const routeName = 'DetailsScreen';

  @override
  State<DetailsScreen> createState() => _DetailsScreenState();
}

class _DetailsScreenState extends State<DetailsScreen> {
  Billionaire? billionaire;
  late TextStyle headingStyle;
  static const methodChannel = MethodChannel("method_channel");

  String data = "";

  bool showBillionaire = false;

  bool isInit = true;
  bool isLoading = true;

  @override
  void initState() {
    super.initState();
    listenToNativeCalls();
  }

  @override
  Widget build(BuildContext context) {
    headingStyle = const TextStyle(fontWeight: FontWeight.bold);

    String? calculatedNetworth;
    if (billionaire != null) {
      calculatedNetworth = Utils.calculateNetworth(billionaire!.finalWorth);
    }

    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            SystemChannels.platform.invokeMethod('SystemNavigator.pop');
          },
        ),
        title: const Text("Details"),
      ),
      body: isLoading
          ? const Center(
              child: CircularProgressIndicator.adaptive(),
            )
          : billionaire == null
              ? const Center(
                  child: Text("Failed to get data"),
                )
              : SingleChildScrollView(
                  child: Padding(
                    padding: const EdgeInsets.all(16),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Column(
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                Stack(
                                  clipBehavior: Clip.none,
                                  children: [
                                    Image.network(
                                      billionaire!.squareImage ?? "",
                                      errorBuilder: ((context, error, stackTrace) => const Center(
                                            child: Icon(Icons.image_not_supported),
                                          )),
                                      height: 100,
                                      width: 100,
                                      fit: BoxFit.cover,
                                    ),
                                    Positioned(
                                      bottom: -10,
                                      right: -10,
                                      child: Container(
                                        height: 35,
                                        width: 35,
                                        decoration: BoxDecoration(
                                          color: Colors.blue.shade800,
                                          borderRadius: BorderRadius.circular(17),
                                        ),
                                        alignment: Alignment.center,
                                        child: Text(
                                          "#${billionaire!.rank}",
                                          style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
                                        ),
                                      ),
                                    )
                                  ],
                                ),
                                const SizedBox(height: 20),
                                Text(
                                  "\$$calculatedNetworth Bn",
                                  style: headingStyle,
                                )
                              ],
                            ),
                            const SizedBox(width: 15),
                            Expanded(
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  const SizedBox(height: 10),
                                  Text(
                                    billionaire!.personName ?? "",
                                    style: headingStyle,
                                    maxLines: 2,
                                  ),
                                  buildBirthDate(),
                                  const SizedBox(height: 5),
                                  Text("${billionaire!.city}, ${billionaire!.countryOfCitizenship}"),
                                  Text(billionaire!.source ?? ""),
                                ],
                              ),
                            ),
                          ],
                        ),
                        const SizedBox(height: 10),
                        ...buildMoreInfo(),
                        ...buildBio(),
                        ...buildAbout(),
                        ...buildFincialAssets(),
                      ],
                    ),
                  ),
                ),
    );
  }

  // ===========================================================================
  Widget buildBirthDate() {
    if (billionaire!.birthDate == null) {
      return Container();
    }

    final birthDate = DateTime.fromMicrosecondsSinceEpoch(billionaire!.birthDate! * 1000);

    final dateOfBirthString = DateFormat.yMMMd().format(birthDate);
    final age = Utils.calculateAge(billionaire!.birthDate);

    return Text("Born: $dateOfBirthString ($age years)");
  }

  buildMoreInfo() {
    if (billionaire!.uri == null) {
      return [];
    }

    final moreInfoLink = "https://www.forbes.com/profile/${billionaire!.uri}";
    return [
      TextButton(
        child: Text("More info: $moreInfoLink"),
        onPressed: () {},
      )
    ];
  }

  buildBio() {
    if (billionaire!.bios == null || billionaire!.bios!.isEmpty) {
      return [];
    }
    String biosString = "";
    for (final bio in billionaire!.bios!) {
      biosString += "$bio\n\n";
    }
    return [
      Text("Bio:", style: headingStyle),
      const SizedBox(height: 5),
      Text(biosString),
    ];
  }

  buildAbout() {
    if (billionaire!.abouts == null || billionaire!.abouts!.isEmpty) {
      return [];
    }
    String aboutsString = "";
    for (final about in billionaire!.abouts!) {
      aboutsString += "$about\n\n";
    }
    return [
      Text("About:", style: headingStyle),
      const SizedBox(height: 5),
      Text(aboutsString),
    ];
  }

  buildFincialAssets() {
    if (billionaire!.financialAssets == null || billionaire!.financialAssets!.isEmpty) {
      return [];
    }
    final List<Widget> assetRows = [
      Text("Financial Assets: ", style: headingStyle),
      const SizedBox(height: 10),
    ];

    for (final asset in billionaire!.financialAssets!) {
      assetRows.add(
        Container(
          margin: const EdgeInsets.only(bottom: 10),
          padding: const EdgeInsets.only(bottom: 15, top: 0),
          width: double.infinity,
          decoration: const BoxDecoration(
            border: Border(
              bottom: BorderSide(width: 0.5),
            ),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    "Asset Name: ",
                    style: headingStyle,
                  ),
                  Expanded(child: Text(asset.companyName!)),
                ],
              ),
              Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    "Shares: ",
                    style: headingStyle,
                  ),
                  Expanded(child: Text("${asset.numberOfShares}")),
                ],
              ),
              Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    "Share Price: ",
                    style: headingStyle,
                  ),
                  Expanded(child: Text("\$${asset.sharePrice}")),
                ],
              ),
            ],
          ),
        ),
      );
    }
    return assetRows;
  }

  void getBillionaireData() async {
    setState(() {
      isLoading = true;
    });

    final data = await methodChannel.invokeMethod("getBillionaireJson");

    if (data != null) {
      setState(() {
        billionaire = Billionaire.fromJson(jsonDecode(data));
      });
    }

    setState(() {
      isLoading = false;
    });
  }

  void listenToNativeCalls() {
    methodChannel.setMethodCallHandler((call) async {
      if (call.method == 'refresh') {
        getBillionaireData();
      }
    });
  }
}
