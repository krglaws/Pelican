package pelican.pelican;

/**
 * Created by Sebastian on 5/9/2018.
 */

public class Video {

    private String videoUrl;
    private String videoOwner;

    public Video(String videoUrl, String videoOwner) {
        this.videoUrl = videoUrl;
        this.videoOwner = videoOwner;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
    public String getVideoOwner(){
        return videoOwner;
    }
    //public void setVideoUrl(String videoUrl) {
    //    this.videoUrl = videoUrl;
    //}

    //public void setVideoOwner(String owner){
     //   this.videoOwner = owner;
    //}

}
