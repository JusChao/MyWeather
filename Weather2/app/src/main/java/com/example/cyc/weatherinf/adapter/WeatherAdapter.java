package com.example.cyc.weatherinf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cyc.weatherinf.R;
import com.example.cyc.weatherinf.module.CityWeather;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cyc on 2016/9/21.
 */
public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private static final String BASE_IMG_URL = "http://files.heweather.com/cond_icon/";

    private static final int TYPE_ONE = 0;
    private static final int TYPE_TWO = 1;
    private static final int TYPE_THREE = 2;
    private static final int TYPE_FOUR = 3;

    private CityWeather cityWeather;

    public WeatherAdapter(Context mContext, CityWeather cityWeather) {
        this.mContext = mContext;
        this.cityWeather = cityWeather;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ONE:
                return new NowWeahterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_now_weather, parent, false));
            case TYPE_TWO:
                return new HourWeatherViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_hour, parent, false));
            case TYPE_THREE:
                return new ForecastViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_forecast, parent, false));
            case TYPE_FOUR:
                return new SuggestionViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_suggestion, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        switch (itemType) {
            case TYPE_ONE:
                ((NowWeahterViewHolder) holder).bind(cityWeather);
                break;
            case TYPE_TWO:
                ((HourWeatherViewHolder) holder).bind(cityWeather);
                break;
            case TYPE_THREE:
                ((ForecastViewHolder) holder).bind(cityWeather);
                break;
            case TYPE_FOUR:
                ((SuggestionViewHolder) holder).bind(cityWeather);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == WeatherAdapter.TYPE_ONE) {
            return WeatherAdapter.TYPE_ONE;
        }
        if (position == WeatherAdapter.TYPE_TWO) {
            return WeatherAdapter.TYPE_TWO;
        }
        if (position == WeatherAdapter.TYPE_THREE) {
            return WeatherAdapter.TYPE_THREE;
        }
        if (position == WeatherAdapter.TYPE_FOUR) {
            return WeatherAdapter.TYPE_FOUR;
        }

        return super.getItemViewType(position);
    }

    /*
    当日天气
     */
    class NowWeahterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.weather_name)
        TextView weatherName;
        @BindView(R.id.now_weather_icon)
        ImageView weahterIcon;
        @BindView(R.id.now_weather_temp)
        TextView weatherTemp;
        @BindView(R.id.now_weather_pm)
        TextView weatherPM;
        @BindView(R.id.now_weather_qul)
        TextView weatherQul;

        public NowWeahterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(CityWeather weather) {

            Glide.with(mContext).load(BASE_IMG_URL + weather.getHeWeather()
                    .get(0).getNow().getCond().getCode()+".png").diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(weahterIcon);
            weatherName.setText(weather.getHeWeather().get(0).getNow().getCond().getTxt());
            weatherTemp.setText(String.format("%s℃",
                    weather.getHeWeather().get(0).getNow().getTmp()));

            try {
                weatherPM.setText(String.format("PM2.5: %sug/m³",
                        weather.getHeWeather().get(0).getAqi().getCity().getPm25()));
                weatherQul.setText(String.format("空气质量: %s",
                        weather.getHeWeather().get(0).getAqi().getCity().getQlty()));
            } catch (NullPointerException e) {
                weatherPM.setText("N/A");
                weatherQul.setText("N/A");
            }

        }
    }

    /*
    小时预报
     */
    class HourWeatherViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout hourLinearLayout;
        private TextView[] mClock = new TextView[cityWeather.getHeWeather().get(0).getHourly_forecast().size()];
        private TextView[] mTemp = new TextView[cityWeather.getHeWeather().get(0).getHourly_forecast().size()];
        private TextView[] mHum = new TextView[cityWeather.getHeWeather().get(0).getHourly_forecast().size()];
        private TextView[] mWind = new TextView[cityWeather.getHeWeather().get(0).getHourly_forecast().size()];

        public HourWeatherViewHolder(View itemView) {
            super(itemView);

            hourLinearLayout = (LinearLayout) itemView.findViewById(R.id.hour_linearlayout);

            for (int i = 0;i<cityWeather.getHeWeather().get(0).getHourly_forecast().size();i++) {
                View view = View.inflate(mContext, R.layout.item_hour_line, null);
                mClock[i] = (TextView) view.findViewById(R.id.hour_clock);
                mTemp[i] = (TextView) view.findViewById(R.id.hour_temp);
                mHum[i] = (TextView) view.findViewById(R.id.hour_hum);
                mWind[i] = (TextView) view.findViewById(R.id.hour_wind);
                hourLinearLayout.addView(view);
            }
        }

        public void bind(CityWeather weather) {
            for (int i=0;i<weather.getHeWeather().get(0).getHourly_forecast().size();i++) {
                String mDate = weather.getHeWeather().get(0).getHourly_forecast().get(i).getDate();
                String mTmp = weather.getHeWeather().get(0).getHourly_forecast().get(i).getTmp();
                mClock[i].setText(mDate.substring(mDate.length() - 5, mDate.length()));
                mTemp[i].setText(String.format("%s℃", weather.getHeWeather().get(0).getHourly_forecast().get(i).getTmp()));
                mHum[i].setText(String.format("%s%%", weather.getHeWeather().get(0).getHourly_forecast().get(i).getHum()));
                mWind[i].setText(String.format("%sKm/h", weather.getHeWeather().get(0).getHourly_forecast().get(i).getWind().getSpd()));
            }
        }
    }

    /*
    行事建议
     */
    class SuggestionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cloth_brief)
        TextView clothBrief;
        @BindView(R.id.cloth_txt)
        TextView clothTxt;
        @BindView(R.id.sport_brief)
        TextView sportBrief;
        @BindView(R.id.sport_txt)
        TextView sportTxt;
        @BindView(R.id.travel_brief)
        TextView travelBrief;
        @BindView(R.id.travel_txt)
        TextView travelTxt;
        @BindView(R.id.flu_brief)
        TextView fluBrief;
        @BindView(R.id.flu_txt)
        TextView fluTxt;


        public SuggestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(CityWeather weather) {
            clothBrief.setText(String.format("穿衣指数---%s", weather.getHeWeather().get(0).getSuggestion().getDrsg().getBrf()));
            clothTxt.setText(weather.getHeWeather().get(0).getSuggestion().getDrsg().getTxt());

            sportBrief.setText(String.format("运动指数---%s", weather.getHeWeather().get(0).getSuggestion().getSport().getBrf()));
            sportTxt.setText(weather.getHeWeather().get(0).getSuggestion().getSport().getTxt());

            travelBrief.setText(String.format("旅游指数---%s", weather.getHeWeather().get(0).getSuggestion().getTrav().getBrf()));
            travelTxt.setText(weather.getHeWeather().get(0).getSuggestion().getTrav().getTxt());

            fluBrief.setText(String.format("感冒指数---%s", weather.getHeWeather().get(0).getSuggestion().getFlu().getBrf()));
            fluTxt.setText(weather.getHeWeather().get(0).getSuggestion().getFlu().getTxt());

        }
    }

    /*
    未来天气
     */
    class ForecastViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.forecast_one_icon)
        ImageView forecastOneIcon;
        @BindView(R.id.forecast_two_icon)
        ImageView forecastTwoIcon;
        @BindView(R.id.forecast_three_icon)
        ImageView forecastThreeIcon;
        @BindView(R.id.forecast_four_icon)
        ImageView forecastFourIcon;
        @BindView(R.id.forecast_five_icon)
        ImageView forecastFiveIcon;
        @BindView(R.id.forecast_six_icon)
        ImageView forecastSixIcon;


        @BindView(R.id.forecast_one)
        TextView forecastOne;
        @BindView(R.id.forecast_one_temp)
        TextView forecastOneTemp;
        @BindView(R.id.forecast_two)
        TextView forecastTwo;
        @BindView(R.id.forecast_two_temp)
        TextView forecastTwoTemp;
        @BindView(R.id.forecast_three)
        TextView forecastThree;
        @BindView(R.id.forecast_three_temp)
        TextView forecastThreeTemp;
        @BindView(R.id.forecast_four)
        TextView forecastFour;
        @BindView(R.id.forecast_four_temp)
        TextView forecastFourTemp;
        @BindView(R.id.forecast_five)
        TextView forecastFive;
        @BindView(R.id.forecast_five_temp)
        TextView forecastFiveTemp;
        @BindView(R.id.forecast_six)
        TextView forecastSix;
        @BindView(R.id.forecast_six_temp)
        TextView forecastSixTemp;


        public ForecastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(CityWeather weather) {
            forecastOne.setText("今日");
            forecastTwo.setText("明日");
            forecastThree.setText("明日");
            forecastFour.setText("明日");
            forecastFive.setText("明日");
            forecastSix.setText("明日");

            ImageView[] imageViews = {forecastOneIcon, forecastTwoIcon, forecastThreeIcon,
                    forecastFourIcon, forecastFiveIcon, forecastSixIcon};

            String[] temps = new String[6];
            for (int i = 0;i<6;i++) {
                temps[i] = weather.getHeWeather().get(0).getDaily_forecast().get(i).getTmp().getMax() + "°/"
                        + weather.getHeWeather().get(0).getDaily_forecast().get(i).getTmp().getMin() + "°";
                Glide.with(mContext).load(BASE_IMG_URL + weather.getHeWeather().get(0).getDaily_forecast().get(i).getCond().getCode_d() + ".png")
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViews[i]);
            }

            forecastOneTemp.setText(temps[0]);
            forecastTwoTemp.setText(temps[1]);
            forecastThreeTemp.setText(temps[2]);
            forecastFourTemp.setText(temps[3]);
            forecastFiveTemp.setText(temps[4]);
            forecastSixTemp.setText(temps[5]);



        }
    }


}