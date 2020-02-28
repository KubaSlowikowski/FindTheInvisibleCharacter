
enum Character {
	PAWELEK(createImagePath("pawelek"), createSoundsArray("Pawelek")),
	NOSACZ(createImagePath("nosacz"), createSoundsArray("Nosacz")),
	JULA(createImagePath("jula"), createSoundsArray("Jula"));
	
	private String imagePath;
	private String[] sounds;
	
	Character(String imagePath, String[] soundsArray) {
		this.imagePath = imagePath;
		this.sounds = soundsArray;
	}
	
	private static String createImagePath(String characterName) {
		return "res//images//" + characterName + ".png";
	}
	
	private static String[] createSoundsArray(String characterName) {
		String[] sounds = new String[7];
		for(int i=5; i>=0; i--) {
			sounds[i] = "\\res\\sounds\\" + characterName + "\\area[" + i + "].wav"; 
		}
		sounds[6] = "\\res\\sounds\\" + characterName + "\\" + "clicked.wav";
		return sounds;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public String getSoundOfIndex(int i) {
		return sounds[i];
	}
}