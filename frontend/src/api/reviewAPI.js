import axios from "axios";

export const onUploadRouteReview = async (routeId, formData) => {
  await axios
    .post(`/route/${routeId}/review`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    })
    .then((res) => true)
    .catch((error) => {
      console.error(error);
      throw new Error(`unExpected Error ${error}`);
    });
};

export const onReceiveRouteReview = async (routeId) => {
  const result = await axios
    .get(`/route/${routeId}/reviews`)
    .then((res) => res.data.reviews)
    .catch((error) => {
      console.log(error);
      throw new Error(`unExpected Error ${error}`);
    });
  return result;
};

export const onEditRouteReview = async (routeId, files, content, score) => {
  const data = {
    files,
    content,
    score,
  };
  return await axios
    .post(`/route/review/${routeId}`, data, {
      headers: { "Content-Type": "multipart/form-data" },
    })
    .then((res) => true)
    .catch((error) => {
      console.error(error);
      throw new Error(`unExpected Error ${error}`);
    });
};

export const onDeleteRouteReview = async (routeId) => {
  await axios
    .delete(`/route/review/${routeId}`)
    .then((res) => true)
    .catch((error) => {
      console.error(error);
      throw new Error(`unHandled error ${error}`);
    });
};

export const onReceivePlaceReview = (placeId) => {
  return axios
    .get(`/place/${placeId}/review`)
    .then((res) => {
      return res.data;
    })
    .catch((error) => {
      console.error(error);
      return false;
    });
};
