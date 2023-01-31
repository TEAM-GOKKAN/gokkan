import React, { useEffect } from 'react';
import { itemDetailAtom, itemIdAtom } from '../store/examineAtom';
import { useAtom } from 'jotai';
import ExamineInfoMain from '../components/Examine/ExamineInfoMain';
import CategoryInfo from '../components/LotDetail/CategoryInfo';
import InfoDetail from '../components/LotDetail/InfoDetail';
import LotDescription from '../components/LotDetail/LotDescription';
import ExamineProductPrice from '../components/Examine/ExamineProductPrice';
import { useParams } from 'react-router-dom';
import ExamineButton from '../components/Examine/ExamineButton';

const ExpertWorkDetailPage = () => {
  const [itemId, setItemId] = useAtom(itemIdAtom);
  const [itemDetail] = useAtom(itemDetailAtom);
  const params = useParams();

  useEffect(() => {
    const newItemId = Number(params.itemId);
    if (itemId !== newItemId) {
      setItemId(newItemId);
    }
  }, []);

  if (itemDetail.id === 8) {
    return <div>Loading</div>;
  }
  return (
    <div>
      <CategoryInfo category={itemDetail.category} />
      <ExamineInfoMain
        itemName={itemDetail.name}
        itemNumber={itemDetail.itemNumber}
        itemImageUrls={itemDetail.imageItemUrls}
        itemImageCheckUrls={itemDetail.imageCheckUrls}
      />
      <InfoDetail
        brand={itemDetail.brand}
        designer={itemDetail.designer}
        material={itemDetail.material}
        period={itemDetail.productionYear}
        size={`${itemDetail.width} x ${itemDetail.depth} x ${itemDetail.height} cm`}
        conditionGrade={itemDetail.conditionGrade}
        conditionDescription={itemDetail.conditionDescription}
      />
      <LotDescription content={itemDetail.text} />
      <ExamineProductPrice itemStartPrice={itemDetail.startPrice} />
      <ExamineButton itemId={itemDetail.id} />
    </div>
  );
};

export default ExpertWorkDetailPage;
