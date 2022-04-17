class Utils {
  static String? calculateNetworth(double? networth) {
    if (networth == null) {
      return null;
    }
    return (networth / 1000).toStringAsFixed(2);
  }

  static String? calculateAge(int? dob) {
    if (dob == null) {
      return null;
    }
    final dateOfBirth = DateTime.fromMicrosecondsSinceEpoch(dob * 1000);
    final difference = DateTime.now().difference(dateOfBirth);
    final years = difference.inDays / 365;
    return years.toStringAsFixed(0);
  }
}
