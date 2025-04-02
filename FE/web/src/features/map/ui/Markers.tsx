import { CustomOverlayMap, MapMarker } from 'react-kakao-maps-sdk';
import { Marker } from '../model/marker';

interface Props {
  markers: Marker[];
  onClick: (marker: Marker) => void;
  selectedMarker: Marker | null;
  imgSrc: string;
}

export default function Markers({ markers, onClick, selectedMarker, imgSrc }: Props) {
  return (
    <>
      {markers.map((marker) => (
        <CustomOverlayMap key={marker.id + 'marker'} position={marker.position}>
          <MapMarker
            onClick={() => onClick(marker)}
            position={marker.position}
            image={{
              src: imgSrc,
              size: {
                width: selectedMarker?.id === marker.id ? 60 : 30,
                height: selectedMarker?.id === marker.id ? 70 : 35,
              },
              options: {
                offset: {
                  x: selectedMarker?.id === marker.id ? 24 : 12,
                  y: selectedMarker?.id === marker.id ? 70 : 35,
                }, // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
                alt: `음식점`,
              },
            }}
          ></MapMarker>
        </CustomOverlayMap>
      ))}
    </>
  );
}
