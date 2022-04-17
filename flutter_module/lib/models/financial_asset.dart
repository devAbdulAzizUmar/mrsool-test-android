class FinancialAsset {
  String? exchange;
  String? ticker;
  String? companyName;
  num? numberOfShares;
  num? sharePrice;
  String? currencyCode;
  dynamic exchangeRate;
  bool? interactive;
  num? currentPrice;
  double? exerciseOptionPrice;

  FinancialAsset({
    exchange,
    ticker,
    companyName,
    numberOfShares,
    sharePrice,
    currencyCode,
    exchangeRate,
    interactive,
    currentPrice,
    exerciseOptionPrice,
  });

  FinancialAsset.fromJson(Map<String, dynamic> json) {
    exchange = json['exchange'];
    ticker = json['ticker'];
    companyName = json['companyName'];
    numberOfShares = json['numberOfShares'];
    sharePrice = json['sharePrice'];
    currencyCode = json['currencyCode'];
    exchangeRate = json['exchangeRate'];
    interactive = json['interactive'];
    currentPrice = json['currentPrice'];
    exerciseOptionPrice = json['exerciseOptionPrice'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['exchange'] = exchange;
    data['ticker'] = ticker;
    data['companyName'] = companyName;
    data['numberOfShares'] = numberOfShares;
    data['sharePrice'] = sharePrice;
    data['currencyCode'] = currencyCode;
    data['exchangeRate'] = exchangeRate;
    data['interactive'] = interactive;
    data['currentPrice'] = currentPrice;
    data['exerciseOptionPrice'] = exerciseOptionPrice;
    return data;
  }
}
