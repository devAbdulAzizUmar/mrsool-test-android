import 'financial_asset.dart';
import 'person.dart';

class Billionaire {
  String? name;
  int? year;
  String? uri;
  String? bio;
  int? rank;
  String? listUri;
  double? finalWorth;
  Person? person;
  bool? visible;
  String? personName;
  String? state;
  String? city;
  String? source;
  List<String>? industries;
  String? countryOfCitizenship;
  int? timestamp;
  int? version;
  String? naturalId;
  int? position;
  bool? imageExists;
  String? gender;
  int? birthDate;
  String? lastName;
  List<FinancialAsset>? financialAssets;
  int? date;
  bool? wealthList;
  double? estWorthPrev;
  double? privateAssetsWorth;
  bool? familyList;
  bool? interactive;
  int? archivedWorth;
  String? thumbnail;
  String? squareImage;
  bool? bioSuppress;
  List<String>? csfDisplayFields;
  List<String>? bios;
  List<String>? abouts;

  Billionaire(
      {name,
      year,
      uri,
      bio,
      rank,
      listUri,
      finalWorth,
      person,
      visible,
      personName,
      state,
      city,
      source,
      industries,
      countryOfCitizenship,
      timestamp,
      version,
      naturalId,
      position,
      imageExists,
      gender,
      birthDate,
      lastName,
      financialAssets,
      date,
      wealthList,
      estWorthPrev,
      privateAssetsWorth,
      familyList,
      interactive,
      archivedWorth,
      thumbnail,
      squareImage,
      bioSuppress,
      csfDisplayFields,
      bios,
      abouts});

  Billionaire.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    year = json['year'];
    uri = json['uri'];
    bio = json['bio'];
    rank = json['rank'];
    listUri = json['listUri'];
    finalWorth = (json['finalWorth'] as num).toDouble();
    person = json['person'] != null ? Person.fromJson(json['person']) : null;
    visible = json['visible'];
    personName = json['personName'];
    state = json['state'];
    city = json['city'];
    source = json['source'];
    industries = json['industries'].cast<String>();
    countryOfCitizenship = json['countryOfCitizenship'];
    timestamp = json['timestamp'];
    version = json['version'];
    naturalId = json['naturalId'];
    position = json['position'];
    imageExists = json['imageExists'];
    gender = json['gender'];
    birthDate = json['birthDate'];
    lastName = json['lastName'];
    if (json['financialAssets'] != null) {
      financialAssets = <FinancialAsset>[];
      json['financialAssets'].forEach((v) {
        financialAssets!.add(FinancialAsset.fromJson(v));
      });
    }
    date = json['date'];
    wealthList = json['wealthList'];
    estWorthPrev = (json['estWorthPrev'] as num).toDouble();
    privateAssetsWorth = (json['privateAssetsWorth'] as num).toDouble();
    familyList = json['familyList'];
    interactive = json['interactive'];
    archivedWorth = json['archivedWorth'];
    thumbnail = json['thumbnail'];
    squareImage = json['squareImage'];
    bioSuppress = json['bioSuppress'];
    csfDisplayFields = json['csfDisplayFields'].cast<String>();
    bios = json['bios']?.cast<String>();
    abouts = json['abouts']?.cast<String>();
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['name'] = name;
    data['year'] = year;
    data['uri'] = uri;
    data['bio'] = bio;
    data['rank'] = rank;
    data['listUri'] = listUri;
    data['finalWorth'] = finalWorth;
    if (person != null) {
      data['person'] = person!.toJson();
    }
    data['visible'] = visible;
    data['personName'] = personName;
    data['state'] = state;
    data['city'] = city;
    data['source'] = source;
    data['industries'] = industries;
    data['countryOfCitizenship'] = countryOfCitizenship;
    data['timestamp'] = timestamp;
    data['version'] = version;
    data['naturalId'] = naturalId;
    data['position'] = position;
    data['imageExists'] = imageExists;
    data['gender'] = gender;
    data['birthDate'] = birthDate;
    data['lastName'] = lastName;
    if (financialAssets != null) {
      data['financialAssets'] = financialAssets!.map((v) => v.toJson()).toList();
    }
    data['date'] = date;
    data['wealthList'] = wealthList;
    data['estWorthPrev'] = estWorthPrev;
    data['privateAssetsWorth'] = privateAssetsWorth;
    data['familyList'] = familyList;
    data['interactive'] = interactive;
    data['archivedWorth'] = archivedWorth;
    data['thumbnail'] = thumbnail;
    data['squareImage'] = squareImage;
    data['bioSuppress'] = bioSuppress;
    data['csfDisplayFields'] = csfDisplayFields;
    data['bios'] = bios;
    data['abouts'] = abouts;
    return data;
  }
}
