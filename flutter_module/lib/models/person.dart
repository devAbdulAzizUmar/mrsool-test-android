class Person {
  String? name;
  String? uri;
  bool? imageExists;
  String? squareImage;

  Person({name, uri, imageExists, squareImage});

  Person.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    uri = json['uri'];
    imageExists = json['imageExists'];
    squareImage = json['squareImage'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['name'] = name;
    data['uri'] = uri;
    data['imageExists'] = imageExists;
    data['squareImage'] = squareImage;
    return data;
  }
}
